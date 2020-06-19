package server;

import exception.SpotifyException;
import gui.SpotifyPartyFrame;
import interfaces.SpotifyPlayerAPI;
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
    private String trackID;
    private long pos;
    private boolean playing;
    ArrayList<DataOutputStream> streams = new ArrayList<>();
    ServerSocket ss;
    Thread reciver;
    Thread sender;

    public TCPServer(int serverPort, boolean diffNetWork)
    {

        try {
            ss = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        api = new SpotifyAppleScriptWrapper();
        trackID = api.getTrackId();
        try {
            pos = api.getPlayerPosition();
        } catch (SpotifyException e) {
            e.printStackTrace();
        }
        try {
            playing = api.isPlaying();
        } catch (SpotifyException e) {
            e.printStackTrace();
        }
        boolean star = true;
        if(diffNetWork) {
            //makes sure the port is clear
            UPnP.closePortTCP((serverPort));
            //only needed if the clients are not on the same network
            star = (UPnP.openPortTCP((serverPort)));
            System.out.println(star);
        }
        startReceiver();
        startSender();
        System.out.println("Server is started!");
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

}