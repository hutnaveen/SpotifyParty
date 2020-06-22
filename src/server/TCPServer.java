package server;

import exception.SpotifyException;
import gui.SpotifyPartyFrame;
import interfaces.SpotifyPlayerAPI;
import main.SpotifyParty;
import spotifyAPI.SpotifyAppleScriptWrapper;
import upnp.UPnP;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class TCPServer
{
    private SpotifyPlayerAPI api;
    private ArrayList<DataOutputStream> streams = new ArrayList<>();
    private ServerSocket ss;
    private Thread reciver;
    private Thread sender;
    private int serverPort = 9009;
    public TCPServer(boolean diffNetWork)
    {
        api = new SpotifyAppleScriptWrapper();

        boolean star;
        if(diffNetWork) {
            for(; serverPort <= 49140; serverPort += 11) {
                //makes sure the port is clear
                UPnP.closePortTCP((serverPort));
                //only needed if the clients are not on the same network
                star = (UPnP.openPortTCP((serverPort)));
                System.out.println(star);
                log("" + star);
                if (star)
                    break;
            }
        }
        try {
            ss = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        startReceiver();
        startSender();
        System.out.println("Server is started!");
        log("Server Started");
        SpotifyPartyFrame.status.setLabel("Guests: 0");
    }
    private void startReceiver()
    {
        reciver = new Thread(() -> {
            while (true) {
                //if client not added to list of clients add it
                Socket s = null;
                DataOutputStream dos = null;
                try {
                    s = ss.accept();
                    dos = new DataOutputStream(s.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("added");
                log("added");
              streams.add(dos);
                SpotifyPartyFrame.status.setLabel("Guests: " + streams.size());
                try {
                        if (dos != null) {
                            dos.writeUTF(api.getTrackId() + " " + api.isPlaying() + " " + api.getPlayerPosition() + " " + System.currentTimeMillis());
                        }
                    } catch (SpotifyException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        });
        reciver.start();
    }
    private void sendToClients(String msg)
    {
        for(int i = 0; i < streams.size(); i++)
        {
            try
            {
                streams.get(i).writeUTF(msg);
            }catch (SocketException e)
            {
                streams.remove(i--);
                SpotifyPartyFrame.status.setLabel("Guests: " + streams.size());
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