package client;
import chatGUI.ChatPanel;
import coroutines.KThreadRepKt;
import gui.*;
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
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    private  String serverIP;
    private int serverPort;
    public BlockingQueue<UpdateData> updateData = new LinkedBlockingQueue<>(1);
    public void sendToServer(String msg) {
        try {
            dos.writeUTF(id + " " + msg.trim());
        }catch (SocketException e){
             int retries = 0;
             boolean status = false;
             do {
                 status = establishConnectionToHost();
             }while(!status && ++retries < 3);
             if(!status && retries > 2){
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
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(id + " " + msg.trim());
    }

    public TCPClient(String serverIP, int serverPort)
    {   this.serverIP = serverIP;
        this.serverPort = serverPort;
        establishConnectionToHost();
    }

    private boolean establishConnectionToHost() {
        try {
            icon = ImageIO.read(Notification.class.getResource("/images/logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Socket s = null;
        try {
            InetAddress ip = InetAddress.getByName(serverIP);
             s = new Socket(ip, serverPort);
            s.setKeepAlive(true);
            s.setSoTimeout(10000);
            dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
            dos = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            try {
                s.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
        try {
            String names = dis.readUTF().replace("[", "").replace("]", "");
            //ChatPanel.addNames(names.split(","));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendToServer("name " + FriendName);
        trackUpdater();
        return false;
    }

    public void quit()
    {
    }
    Runnable updateRun;
    private void trackUpdater() {
        KThreadRepKt.startInfCor(() -> {
            try {
                System.out.println("checking update data");
                UpdateData tempData = updateData.poll(5000, TimeUnit.MILLISECONDS);
                if (tempData != null) {
                    updatePlayer(tempData.getTrackID(), tempData.isPlaying(), tempData.getPos(), tempData.getTimeStamp());
                }
                else
                {
                    System.out.println("restarted");
                    updater.cancel(null);
                    updater = KThreadRepKt.startInfCor(updateRun);
                }
            } catch (Throwable e) {
                e.printStackTrace();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(playerData != null && playerData.length >0) {
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
                        //updateData.drainTo(new ArrayList<>(1));
                        updateData.offer(new UpdateData(finalPlayerData[0].trim(), finalPlayerData[1].trim().substring(0, 4).startsWith("tru"), t, fact));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } catch (Throwable e) {
                        System.out.println("Throwable caught");
                        e.printStackTrace();
                    }
                }
            }
        };
        // updater = new Thread(updateRun);
        updater = KThreadRepKt.startInfCor(updateRun);
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
                            SpotifyParty.api.setPlayBackPosition(pos + (TimeUtils.getAppleTime() - timeStamp) + 500);
                        }
                        if (Math.abs((TimeUtils.getAppleTime() - timeStamp) + pos - tempPos) > 2000) {
                            System.out.println("Time: " + pos + " Player: " + tempPos);
                            log("Time: " + pos + " Player: " + tempPos);
                            SpotifyParty.api.setPlayBackPosition(pos + (TimeUtils.getAppleTime() - timeStamp) + 500);
                        }
                    }
                } else if (!ad || time >= pos) {
                    ad = true;
                    SpotifyParty.api.pause();
                    time = pos;
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