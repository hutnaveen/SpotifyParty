package server;

import chatGUI.Chat;
import chatGUI.ChatPanel;
import chatGUI.RequestTab;
import chatGUI.SpotifyPartyPanelChat;
import exception.SpotifyException;
import gui.SpotifyPartyFrame;
import gui.SpotifyPartyPanel;
import interfaces.SpotifyPlayerAPI;
import main.SpotifyParty;
import model.Streams;
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
import java.util.HashMap;
import java.util.HashSet;

import static chatGUI.ChatPanel.names;

public class TCPServer
{
    private final SpotifyPlayerAPI api;
    public static HashMap<String, Streams> streams = new HashMap<>();
    private ServerSocket ss;
    private Thread reciver;
    private Thread sender;
    private int serverPort = 9000;
   String last;
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
        names.add("HOST");
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
                String id = "" + s.getLocalAddress().getHostAddress() + s.getInetAddress().getHostAddress();
                try {
                    s = ss.accept();
                    dos = new DataOutputStream(s.getOutputStream());
                    in = new DataInputStream(new BufferedInputStream(s.getInputStream()));
                    new ClientListener(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("added");
                log("added");
                streams.put(id, new Streams(in, dos));
                try {
                    if (dos != null) {
                        dos.writeUTF(names.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
               /* try {
                    String it = in.readUTF();
                    ChatPanel.addNames(it);
                    sendToClients("usr " + it);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        });
        reciver.start();
    }
    public static void sendToClients(String msg, String exc)
    {
        ArrayList<String> rem = new ArrayList<>();
        for(String id: streams.keySet())
        {
            if(!id.equals(exc)) {
                try {
                    streams.get(id).getOutStream().writeUTF(msg);
                } catch (SocketException e) {
                    rem.add(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        for(String id: rem)
            streams.remove(id);
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
                    if(!tempTrack.contains(":ad:") && !tempTrack.isBlank() && !tempTrack.equals("ice")) {
                        sendToClients(tempTrack + " " + api.isPlaying() + " " + api.getPlayerPosition() + " " + System.currentTimeMillis(), null);
                        if(!tempTrack.equals(last)) {
                            last = tempTrack;
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
class ClientListener implements Runnable
{
    private String id;
    Thread t = new Thread(this);
    public ClientListener(String id)
    {
        this.id = id;
        t.start();
    }

    @Override
    public void run() {
        while (true)
        {
            try {
                String[] str = TCPServer.streams.get(id).getInStream().readUTF().trim().split(" ");
                switch (str[1])
                {
                    case "usr":
                        ChatPanel.addNames(str[2].trim());
                        TCPServer.sendToClients("usr " + str[2].trim(), str[0].trim());
                        break;
                    case "request":
                        ChatPanel.chat.addRequest(new RequestTab(str[2].trim(), str[3].trim()));
                        TCPServer.sendToClients("request " + str[2].trim() + " " + str[3].trim(), str[0].trim());
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}