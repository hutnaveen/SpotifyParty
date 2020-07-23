package server;

import exception.SpotifyException;
import chatGUI.ChatPanel;
import gui.RequestTab;
import gui.Requests;
import interfaces.SpotifyPlayerAPI;
import model.PlayerData;
import spotifyAPI.SpotifyAppleScriptWrapper;
import utils.TimeUtils;
import upnp.UPnP;
import utils.NetworkUtils;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static chatGUI.ChatPanel.names;
import static utils.GUIUtils.resizeIcon;
import static main.SpotifyParty.chatPanel;
import static main.SpotifyParty.api;

public class TCPServer
{
    public static HashMap<DataInputStream, DataOutputStream> stream = new HashMap<>();
    private ServerSocket ss;
    private Thread reciver;
    private Thread sender;
    private int serverPort = 9000;
    String last;
    public TCPServer(boolean diffNetWork)
    {
        boolean star;
        if(UPnP.isUPnPAvailable())
            System.out.println("gucci");
        else {
            Object[] options = {"OK"};
            JFrame frame = new JFrame();
            frame.setAlwaysOnTop(true);
            JOptionPane.showOptionDialog(frame, "Looks like the internet your connected to does not support port forwarding.\n No worries you can still join a party and host a local party.", "Can't Start Public Party",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
                    resizeIcon(new ImageIcon(ChatPanel.class.getResource("/images/logo.png")), 50, 50), options, options[0]);
            System.exit(69);
        }
        if(diffNetWork) {
            for(; serverPort <= 9100; serverPort ++) {
                //only needed if the clients are not on the same network
                UPnP.closePortUDP(serverPort);
                UPnP.waitInit();
                star = (UPnP.openPortTCP((serverPort)));
                System.out.println(star);
                if (star)
                    break;
                else
                    UPnP.closePortTCP((serverPort));
            }
        }
        try {
            ss = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //SpotifyPartyPanel.host.setCode(NetworkUtils.simpleEncode(NetworkUtils.getPublicIP(), serverPort, 0));
        ChatPanel.setCode(NetworkUtils.simpleEncode(NetworkUtils.getPublicIP(), serverPort, 0));
        names.add("HOST");
        startConnector();
        startSender();
        System.out.println("Server is started!");

    }


    private void startConnector()
    {
        reciver = new Thread(() -> {
            while (true) {
                //if client not added to list of clients add it
                Socket s = null;
                DataOutputStream dos = null;
                DataInputStream in = null;
                try {
                    s = ss.accept();
                    dos = new DataOutputStream(s.getOutputStream());
                    in = new DataInputStream(new BufferedInputStream(s.getInputStream()));
                    System.out.println(NetworkUtils.getLocalIP());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("added");
                stream.put(in, dos);
                try {
                    if (dos != null) {
                        dos.writeUTF(names.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    String it = in.readUTF();
                    sendToClients("usr " + it);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new ClientListener(in);
            }

        });
        reciver.start();
    }
    public static void sendToClients(String msg)
    {
        sendToClients(msg, null);
    }
    public static void sendToClients(String msg, DataInputStream exc)
    {
        System.out.println(msg);
        ArrayList<DataInputStream> rem = new ArrayList<>();
        for(DataInputStream id: stream.keySet())
        {
            System.out.println(id);
            if(!id.equals(exc)) {
                try {
                    stream.get(id).writeUTF(msg);
                } catch (SocketException e) {
                    rem.add(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        for(DataInputStream id: rem)
            stream.remove(id);
    }
    public void quit()
    {
        sender.stop();
        reciver.stop();
        System.out.println("Server Stopped");
    }
    private void startSender()
    {
        sender = new Thread(() -> {
            while (true) {
                try {
                    PlayerData playerData = api.getPlayerData();
                    String tempTrack = playerData.getItem().getUri();
                    System.out.println("track url: " + tempTrack);
                    if(tempTrack.contains(":ad:")) {
                        System.out.println("AD:before sendToClients");
                        sendToClients(tempTrack + " false " + playerData.getProgress_ms() + " " + playerData.getItem().getDuration_ms());
                        System.out.println("AD:after sendToClients");
                    }
                    if(!tempTrack.isBlank() && !tempTrack.equals("ice")) {
                        System.out.println("before sendToClients");
                        sendToClients(tempTrack + " " + playerData.is_playing() + " " +playerData.getProgress_ms() + " " + TimeUtils.getAppleTime());
                        System.out.println("after sendToClients");
                        if(!tempTrack.equals(last)) {
                            try {
                                System.out.println("before updateData");
                                chatPanel.updateData(tempTrack);
                                last = tempTrack;
                                System.out.println("after updateData");
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    System.out.println("before sleep");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        sender.start();
    }

    public int getServerPort() {
        return serverPort;
    }
}
