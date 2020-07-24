package client;
import chatGUI.ChatPanel;
import gui.*;
import exception.SpotifyException;
import main.SpotifyParty;
import utils.TimeUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static main.SpotifyParty.api;
import static utils.GUIUtils.resizeIcon;
import static chatGUI.SpotifyPartyPanelChat.FriendName;
import static chatGUI.SpotifyPartyPanelChat.spfc;
import static main.SpotifyParty.chatPanel;

public class TCPClient
{
    private DataInputStream dis;
    private DataOutputStream dos;
    private Thread updater;
    private Thread tempUpdate;
    private String id = FriendName;
    private BufferedImage icon;
    public static String prevSong = null;

    public void sendToServer(String msg) {
        try {
            dos.writeUTF(id + " " + msg.trim());
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getMessage().contains("Broken pipe"))
            {
                spfc.setVisible(false);
                Object[] options = {"OK"};
                JFrame frame = new JFrame();
                frame.setAlwaysOnTop(true);
                JOptionPane.showOptionDialog(frame, "The host ended the party", "Party ended",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
                        resizeIcon(new ImageIcon(ChatPanel.class.getResource("/images/logo.png")), 50, 50), options, options[0]);
                System.exit(8);
            }
        }
        System.out.println(id + " " + msg.trim());
    }

    public TCPClient(String serverIP, int serverPort)
    {
        try {
            icon = ImageIO.read(Notification.class.getResource("/images/logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InetAddress ip = InetAddress.getByName(serverIP);
            Socket s = new Socket(ip, serverPort);
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String names = dis.readUTF().replace("[", "").replace("]", "");
            //ChatPanel.addNames(names.split(","));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendToServer("name " + FriendName);
        trackUpdater();
    }
    public void quit()
    {
        updater.stop();
        tempUpdate.stop();
    }
    private void trackUpdater() {
        updater = new Thread(() -> {
            while (true) {
                String[] playerData = null;
                String org = "";
                try {
                    org = dis.readUTF();
                    playerData = org.split(" ");
                } catch (java.io.EOFException e) {
                    quit();
                    System.exit(-2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (playerData[0].equals("usr")) {
                    //ChatPanel.addNames(playerData[1]);
                }
                if (playerData[0].equals("delete"))
                {
                    Requests.redraw(playerData[1]);
                    ChatPanel.chat.revalidate();
                }
                else if(playerData[0].equals("request"))
                {
                    Requests.addRequest(new RequestTab(playerData[1], playerData[2]));
                    ChatPanel.chat.revalidate();
                    //Chat.redraw("");
                }
                else if(playerData[0].equals("addAll"))
                {
                     ArrayList<RequestTab> tabs = new ArrayList<>();
                    for(String rec: playerData[1].split(","))
                    {
                        String[] dat = rec.split(";");
                        tabs.add(new RequestTab(dat[0], dat[1]));
                    }
                    Requests.requestTabs = tabs;
                    Requests.redraw("");
                }
                else if(playerData[0].equals("chat"))
                {
                    org = org.substring(org.indexOf(' ')+1);
                    String name = org.substring(0, org.indexOf(' ')+1);
                    String message = org.substring(org.indexOf(' ')+1);
                    ChatPanel.chat.addText(message, name);
                }
                else{
                    try {
                        long fact = Long.parseLong(playerData[3].trim());
                        System.out.println((Arrays.toString(playerData)) + " " + new Date(TimeUtils.getAppleTime()) + " " + new Date(fact));
                        long t = Long.parseLong(playerData[2].trim());
                        String[] finalPlayerData = playerData;
                        if (tempUpdate != null) {
                            tempUpdate.stop();
                            tempUpdate = new Thread(() -> updatePlayer(finalPlayerData[0].trim(), finalPlayerData[1].trim().substring(0, 4).startsWith("tru"), t, fact));
                            tempUpdate.start();
                        } else {
                            tempUpdate = new Thread(() -> updatePlayer(finalPlayerData[0].trim(), finalPlayerData[1].trim().substring(0, 4).startsWith("tru"), t, fact));
                            tempUpdate.start();
                        }

                    } catch (Exception e) {
                       e.printStackTrace();
                    }
                }
            }
        });
        updater.start();
    }
    boolean ad = false;
    boolean change = false;
    long time = Integer.MIN_VALUE;
    private void updatePlayer(String trackID, boolean playing, long pos, long timeStamp) {
        try {
            //System.out.println(api.getPlayerData().getDevice().is_private_session());
            if (false && (prevSong == null || !prevSong.equals(trackID)))
            {
                chatPanel.updateData(trackID);
                prevSong = trackID;
            }
            else if(!false) {
                String tempTrack = SpotifyParty.api.getTrackUri();
                boolean tempPlaying = SpotifyParty.api.isPlaying();
                log("" + tempPlaying);
                long tempPos = SpotifyParty.api.getPlayBackPosition();
                if (!tempTrack.contains(":ad:")) {
                    ad = false;
                    time = Integer.MIN_VALUE;
                    if (trackID.equals("ice")) {
                        SpotifyParty.api.pause();
                    } else {
                        if (!tempTrack.equals(trackID)) {
                            change = false;
                            SpotifyParty.api.playTrack(trackID);
                            try {
                                System.out.println("update to " + api.getTrackInfo(trackID));
                                chatPanel.updateData(trackID);
                                change = true;
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                change = false;
                            }
                            System.out.println(pos + (TimeUtils.getAppleTime() - timeStamp) + 2000);
                            SpotifyParty.api.setPlayBackPosition(pos + (TimeUtils.getAppleTime() - timeStamp) + 1500);
                        }
                        if(!change)
                        {
                            try {
                                chatPanel.updateData(trackID);
                                change = true;
                            }catch (Exception e)
                            {
                                change = false;
                            }
                        }
                        if(tempPlaying != playing)
                        {
                            if (playing)
                                SpotifyParty.api.play();
                            else
                                SpotifyParty.api.pause();
                            SpotifyParty.api.setPlayBackPosition(pos + (TimeUtils.getAppleTime() - timeStamp) + 1500);
                        }
                        if (Math.abs((TimeUtils.getAppleTime() - timeStamp) + pos - tempPos) > 2000) {
                            System.out.println("Time: " + pos + " Player: " + tempPos);
                            log("Time: " + pos + " Player: " + tempPos);
                            SpotifyParty.api.setPlayBackPosition(pos + (TimeUtils.getAppleTime() - timeStamp) + 1500);
                        }
                    }
                } else if (!ad || time >= pos) {
                    ad = true;
                    SpotifyParty.api.pause();
                    time = pos;
                    Notification notif = new Notification(icon, "SpotifyParty", "ADVERTISEMENT", "The host is playing an ad", 5000);
                    notif.send();
                    System.out.println("mans playing an add");
                    log("an add is playing");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean log(String msg)
    {
        return true;
    }
}