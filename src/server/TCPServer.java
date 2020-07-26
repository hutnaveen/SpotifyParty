package server;

import coroutines.KThreadRepKt;
import chatGUI.ChatPanel;
import kotlin.Unit;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.Job;
import model.PlayerData;
import org.junit.internal.runners.statements.RunAfters;
import utils.TimeUtils;
import upnp.UPnP;
import utils.NetworkUtils;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static chatGUI.ChatPanel.names;
import static utils.GUIUtils.resizeIcon;
import static main.SpotifyParty.chatPanel;
import static main.SpotifyParty.api;

public class TCPServer
{
    public static HashMap<DataInputStream, DataOutputStream> stream = new HashMap<>();
    private ServerSocket ss;
    private Thread reciver;
    private Job sender;
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
        checkIfWorking();
        System.out.println("Server is started!");

    }
    Runnable senderRun;
    private void startConnector()
    {
        KThreadRepKt.startCor(() -> {
                //if client not added to list of clients add it
                Socket s = null;
                DataOutputStream dos = null;
                DataInputStream in = null;
                try {
                    s = ss.accept();
                    dos = new DataOutputStream((s.getOutputStream()));
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
        });
       // reciver.start();
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
        reciver.stop();
        System.out.println("Server Stopped");
    }
    private void startSender()
    {
        senderRun = () -> {
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
                        dat.drainTo(new ArrayList<>(1));
                        dat.offer("updated");
                        System.out.println("after sendToClients");
                        if(!tempTrack.equals(last)) {
                            try {
                                System.out.println("before updateData");
                                chatPanel.updateData(tempTrack);
                                last = tempTrack;
                                System.out.println("after updateData");
                            }catch (Exception e)
                            {
                                dat.drainTo(new ArrayList<>(1));
                                dat.offer("exception");
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    dat.drainTo(new ArrayList<>(1));
                    dat.offer("updated");
                    e.printStackTrace();
                }
                try {
                    System.out.println("before sleep");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        };
        sender = KThreadRepKt.startCor(senderRun);
        //sender.start();
    }
    BlockingQueue<String> dat = new LinkedBlockingQueue<>(1);
    public void checkIfWorking()
    {
        KThreadRepKt.startCor(() -> {
                try {
                    String s = dat.poll(5000, TimeUnit.MILLISECONDS);
                    if(s == null)
                    {
                        sender.cancel(null);
                        sender = KThreadRepKt.startCor(senderRun);
                    }
                } catch (InterruptedException e) {
                    sender.cancel(null);
                    sender = KThreadRepKt.startCor(senderRun);
                    e.printStackTrace();
                }
        });
    }

    public int getServerPort() {
        return serverPort;
    }
}
