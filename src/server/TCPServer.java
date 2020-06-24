package server;

import chatGUI.ChatPanel;
import exception.SpotifyException;
import gui.SpotifyPartyFrame;
import interfaces.SpotifyPlayerAPI;
import main.SpotifyParty;
import spotifyAPI.SpotifyAppleScriptWrapper;
import upnp.UPnP;
import utils.NetworkUtils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import static chatGUI.ChatPanel.names;

public class TCPServer
{
    private final SpotifyPlayerAPI api;
    private  ArrayList<DataOutputStream> outStreams = new ArrayList<>();
    private ArrayList<DataInputStream> inStream = new ArrayList<>();
    private ServerSocket ss;
    private Thread reciver;
    private Thread sender;
    private int serverPort = 9000;
    public TCPServer(boolean diffNetWork)
    {
        api = new SpotifyAppleScriptWrapper();

        boolean star;
        if(diffNetWork) {
            for(; serverPort <= 9100; serverPort ++) {
                //only needed if the clients are not on the same network
                star = (UPnP.openPortTCP((serverPort)));
                System.out.println(star);
                log("" + star);
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
        startConnector();
        startSender();
        System.out.println("Server is started!");
        log("Server Started");
        SpotifyPartyFrame.status.setLabel("Guests: 0");
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("added");
                log("added");
                inStream.add(in);
                SpotifyPartyFrame.status.setLabel("Guests: " + outStreams.size());
                try {
                        if (dos != null) {
                            dos.writeUTF(names.toString());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                try {
                    Thread.sleep(2000);
                    String it = in.readUTF();
                    ChatPanel.addNames(it);
                    sendToClients("usr " + it);
                    outStreams.add(dos);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        reciver.start();
    }
    private void startReceiver()
    {
        String str[] = null;
        while (true) {
            for (DataInputStream s : inStream) {
                try {
                    str = s.readUTF().split(" ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // TODO: 6/23/20
                if(str[0].equals("message")){
                    sendToClients("message " +str[1]);
                }
            }
        }
    }
    private void sendToClients(String msg)
    {
        for(int i = 0; i < outStreams.size(); i++)
        {
            try
            {
                outStreams.get(i).writeUTF(msg);
            }catch (SocketException e)
            {
                outStreams.remove(i--);
                SpotifyPartyFrame.status.setLabel("Guests: " + outStreams.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void quit()
    {
        sender.stop();
        reciver.stop();
        try {
            SpotifyParty.writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SpotifyPartyFrame.status.setLabel("Waiting");
        System.out.println("Server Stopped");
    }
    private void startSender()
    {
        sender = new Thread(() -> {
            while (true) {
                try {
                    String tempTrack = api.getTrackId();
                    if(!tempTrack.contains(":ad:") && !tempTrack.isBlank() && !tempTrack.equals("ice"))
                        sendToClients(tempTrack + " " + api.isPlaying() + " " + api.getPlayerPosition() + " " + System.currentTimeMillis());
                } catch (SpotifyException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        sender.start();
    }
    private boolean log(String msg)
    {
        if(SpotifyParty.writer != null) {
            try {
                SpotifyParty.writer.append(msg).append("\n");
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    public int getServerPort() {
        return serverPort;
    }
}