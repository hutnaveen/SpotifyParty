package client;
import chatGUI.ChatPanel;
import coroutines.KThreadRepKt;
import gui.*;
import exception.SpotifyException;
import kotlinx.coroutines.Job;
import main.SpotifyParty;
import model.PlayerData;
import model.UpdateData;
import utils.TimeUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static main.SpotifyParty.api;
import static utils.GUIUtils.resizeIcon;
import static chatGUI.SpotifyPartyPanelChat.FriendName;
import static chatGUI.SpotifyPartyPanelChat.spfc;
import static main.SpotifyParty.chatPanel;

public class TCPClient
{
    private DataInputStream dis;
    private DataOutputStream dos;
    private Job updater;
    private String id = FriendName;
    private BufferedImage icon;
    public static String prevSong = null;
    public BlockingQueue<UpdateData> updateData = new LinkedBlockingQueue<>(1);
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
            dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
            dos = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
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
    }
    Runnable updateRun;
    private void trackUpdater() {
        KThreadRepKt.startCor(new Runnable() {
            @Override
            public void run() {
                    try {
                        UpdateData tempData = updateData.poll(5000, TimeUnit.MILLISECONDS);
                        if (tempData != null) {
                            updatePlayer(tempData.getTrackID(), tempData.isPlaying(), tempData.getPos(), tempData.getTimeStamp());
                        }
                        else
                        {
                           updater.cancel(null);
                           updater = KThreadRepKt.startCor(updateRun);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        });
        updateRun = () -> {
                System.out.println("Updater Thread alive");
                String[] playerData = null;
                String org = "";
                try {
                    System.out.println("Updater read start");
                    org = dis.readUTF();
                    System.out.println("Updater read done");
                    playerData = org.split(" ");
                } catch (java.io.EOFException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (playerData[0].equals("usr")) {
                    //ChatPanel.addNames(playerData[1]);
                }
                if (playerData[0].equals("delete")) {
                    Requests.redraw(playerData[1]);
                    ChatPanel.chat.revalidate();
                } else if (playerData[0].equals("request")) {
                    Requests.addRequest(new RequestTab(playerData[1], playerData[2]));
                    ChatPanel.chat.revalidate();
                    //Chat.redraw("");
                } else if (playerData[0].equals("addAll")) {
                    ArrayList<RequestTab> tabs = new ArrayList<>();
                    for (String rec : playerData[1].split(",")) {
                        String[] dat = rec.split(";");
                        tabs.add(new RequestTab(dat[0], dat[1]));
                    }
                    Requests.requestTabs = tabs;
                    Requests.redraw("");
                } else if (playerData[0].equals("chat")) {
                    org = org.substring(org.indexOf(' ') + 1);
                    String name = org.substring(0, org.indexOf(' ') + 1);
                    String message = org.substring(org.indexOf(' ') + 1);
                    ChatPanel.chat.addText(message, name);
                } else {
                    try {
                        playerData = org.replace("=", " ").trim().split(" ");
                        long fact = Long.parseLong(playerData[3].trim());
                        System.out.println((Arrays.toString(playerData)) + " " + new Date(TimeUtils.getAppleTime()) + " " + new Date(fact));
                        long t = Long.parseLong(playerData[2].trim());
                        String[] finalPlayerData = playerData;
                        updateData.drainTo(new ArrayList<>(1));
                        updateData.offer(new UpdateData(finalPlayerData[0].trim(), finalPlayerData[1].trim().substring(0, 4).startsWith("tru"), t, fact));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } catch (Throwable e) {
                        System.out.println("Throwable caught");
                        e.printStackTrace();
                    }
                }
        };
       // updater = new Thread(updateRun);
        updater = KThreadRepKt.startCor(updateRun);
        //updater.start();
        //tempUpdate.start();
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
                PlayerData data = api.getPlayerData();
                String tempTrack = data.getItem().getUri();
                boolean tempPlaying = data.is_playing();
                log("" + tempPlaying);
                long tempPos = data.getProgress_ms();
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
                                //System.out.println("update to " + api.getTrackInfo(trackID));
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