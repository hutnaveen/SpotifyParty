package server;

import exception.SpotifyException;
import gui.ChatPanel;
import gui.RequestTab;
import gui.Requests;
import gui.SpotifyPartyPanelChat;
import interfaces.SpotifyPlayerAPI;
import main.SpotifyParty;
import model.Streams;
import spotifyAPI.SpotifyAppleScriptWrapper;
import upnp.UPnP;
import utils.NetworkUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static gui.ChatPanel.names;


public class TCPServer
{
    private final SpotifyPlayerAPI api;
    public static HashMap<DataInputStream, DataOutputStream> stream = new HashMap<>();
    private ServerSocket ss;
    private Thread reciver;
    private Thread sender;
    private int serverPort = 9000;
    String last;
    public TCPServer(boolean diffNetWork)
    {
        api = new SpotifyAppleScriptWrapper();
        boolean star;
        if(UPnP.isUPnPAvailable())
            System.out.println("gucci");
        else
            System.exit(69);
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
        ArrayList<DataInputStream> rem = new ArrayList<>();
        for(DataInputStream id: stream.keySet())
        {
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
                    String tempTrack = api.getTrackUri();
                    if(tempTrack.contains(":ad:")) {
                        sendToClients(tempTrack + " false " + api.getPlayBackPosition() + " " + api.getDuration());
                    }
                    if(!tempTrack.isBlank() && !tempTrack.equals("ice")) {
                        sendToClients(tempTrack + " " + api.isPlaying() + " " + api.getPlayBackPosition() + " " + System.currentTimeMillis());
                        if(!tempTrack.equals(last)) {
                            last = tempTrack;
                            try {
                                SpotifyParty.chatPanel.updateData(tempTrack);
                            }catch (Exception e)
                            {
                                new Thread(() -> {
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException interruptedException) {
                                        interruptedException.printStackTrace();
                                    }
                                    try
                                    {
                                        SpotifyParty.chatPanel.updateData(tempTrack);

                                    }catch (Exception e1)
                                    {

                                    }
                                }).start();
                            }
                        }
                    }
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

    public int getServerPort() {
        return serverPort;
    }
}
class ClientListener implements Runnable
{
    DataInputStream in;
    Thread t = new Thread(this);
    public ClientListener(DataInputStream id)
    {
        in = id;
        t.start();
    }

    @Override
    public void run() {
        while (true)
        {
            try {
                String org = in.readUTF().trim();
                String[] str = org.split(" ");
                System.out.println(Arrays.toString(str));
                switch (str[1])
                {
                    case "usr":
                        TCPServer.sendToClients("usr " + str[2].trim(),in);
                        break;
                    case "request":
                        Requests.addRequest(new RequestTab(str[2].trim(), str[3].trim()));
                        TCPServer.sendToClients("request " + str[2].trim() + " " + str[3].trim(), in);
                        break;
                    case "chat":
                        org = org.substring(org.indexOf(' ')+1);
                        org = org.substring(org.indexOf(' ')+1);
                        String name = org.substring(0, org.indexOf(' '));
                        String message = org.substring(org.indexOf(' ')+1);
                        ChatPanel.chat.addText(message, name);
                        TCPServer.sendToClients("chat " + name+ " " + message, in);
                        break;
                }
            } catch (Exception e) {
                t.stop();
            }
        }
    }
}