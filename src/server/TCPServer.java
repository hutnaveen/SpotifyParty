package server;

import chatGUI.ChatPanel;
import chatGUI.SpotifyPartyPanelChat;
import exception.SpotifyException;
import gui.SpotifyPartyPanel;
import interfaces.SpotifyPlayerAPI;
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
    private String prev;
    public TCPServer(boolean diffNetWork)
    {
        api = new SpotifyAppleScriptWrapper();
        prev = api.getTrackId();
        boolean star;
        if(diffNetWork) {
            /*
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
            */
        }
        try {
            ss = new ServerSocket(9006);
        } catch (IOException e) {
            e.printStackTrace();
        }
       //SpotifyPartyPanel.host.setCode(NetworkUtils.simpleEncode(NetworkUtils.getPublicIP(), serverPort, 0));
        ChatPanel.setCode(NetworkUtils.simpleEncode(NetworkUtils.getPublicIP(), serverPort, 0));
        startConnector();
        startSender();
        System.out.println("Server is started!");
        log("Server Started");
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
                try {
                        if (dos != null) {
                            dos.writeUTF(names.toString());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                try {
                    String it = in.readUTF();
                    ChatPanel.addNames(it);
                    System.out.println(it);
                    sendToClients("usr " + it);
                    outStreams.add(dos);
                } catch (IOException e) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                    String tempTrack = api.getTrackId();
                    if(!tempTrack.contains(":ad:") && !tempTrack.isBlank() && !tempTrack.equals("ice")) {
                        sendToClients(tempTrack + " " + api.isPlaying() + " " + api.getPlayerPosition() + " " + System.currentTimeMillis());
                        if(!tempTrack.equals(prev))
                        {
                            prev = tempTrack;
                            SpotifyPartyPanelChat.chatPanel.updateData(tempTrack);
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
    private boolean log(String msg)
    {
        /*if(SpotifyParty.writer != null) {
            try {
                SpotifyParty.writer.append(msg).append("\n");
            } catch (IOException e) {
                return false;
            }
        }
        return true;*/
        return true;
    }

    public int getServerPort() {
        return serverPort;
    }
}