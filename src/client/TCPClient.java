package client;

import chatGUI.ChatPanel;
import chatGUI.JoinPartyPanel;
import chatGUI.SpotifyPartyPanelChat;
import exception.SpotifyException;
import gui.SpotifyPartyFrame;
import interfaces.SpotifyPlayerAPI;
import main.SpotifyParty;
import model.TrackInfo;
import spotifyAPI.SpotifyAppleScriptWrapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;

public class TCPClient
{
    private SpotifyPlayerAPI api;
    private boolean autoPause = false;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Thread updater;
    private Thread tempUpdate;
    public TCPClient(String serverIP, int serverPort)
    {
        api = new SpotifyAppleScriptWrapper();
        try {
            InetAddress ip = InetAddress.getByName(serverIP);
            Socket s = new Socket(ip, serverPort);
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SpotifyPartyFrame.status.setLabel("Connected!");
        try {
            String names = dis.readUTF().replace("[", "").replace("]", "");
            ChatPanel.addNames(names.split(","));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dos.writeUTF(JoinPartyPanel.name.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        trackUpdater();
    }
    public void quit()
    {
        updater.stop();
        tempUpdate.stop();
        try {
            SpotifyParty.writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SpotifyPartyFrame.status.setLabel("Welcome");
    }
    private void trackUpdater() {
        updater = new Thread(() -> {
            while (true) {
                String[] playerData = null;
                try {
                    playerData = dis.readUTF().split(" ");
                } catch (java.io.EOFException e) {
                    quit();
                    System.exit(-2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (playerData[0].equals("usr")) {
                    ChatPanel.addNames(playerData[1]);
                } else {
                    try {
                        long fact = Long.parseLong(playerData[3].trim());
                        System.out.println((Arrays.toString(playerData)) + " " + new Date(System.currentTimeMillis()) + " " + new Date(fact));
                        log((Arrays.toString(playerData)) + " " + new Date(System.currentTimeMillis()) + " " + new Date(fact));
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

    private void updatePlayer(String trackID, boolean playing, long pos, long timeStamp) {
        try {
            String tempTrack = api.getTrackId();
            boolean tempPlaying = api.isPlaying();
            log(""+tempPlaying);
            long tempPos = api.getPlayerPosition();
            if (!tempTrack.contains(":ad:")) {
                if (trackID.equals("ice")) {
                    api.pause();
                    autoPause = true;
                } else {
                    if ((tempPlaying || autoPause) && !tempTrack.equals(trackID)) {
                        api.playTrack(trackID);
                        TrackInfo m= SpotifyPartyPanelChat.chatPanel.updateData(trackID);
                        SpotifyPartyPanelChat.chatPanel.setColor(m.getDominantColor());
                        if (!tempPlaying)
                            api.play();
                        System.out.println(pos + (System.currentTimeMillis() - timeStamp) + 2000);
                        log(""+pos + (System.currentTimeMillis() - timeStamp) + 2000);
                    } else if (!playing) {
                        if (tempPlaying) {
                            api.pause();
                            autoPause = true;
                        }
                    } else {
                        if (autoPause) {
                            api.play();
                            autoPause = false;
                        }
                    }
                   if (tempPlaying && Math.abs((System.currentTimeMillis() - timeStamp) + pos - tempPos) > 2000) {
                        System.out.println("Time: " + pos + " Player: " + tempPos);
                        log("Time: " + pos + " Player: " + tempPos);
                        api.setPlayBackPosition(pos + (System.currentTimeMillis() - timeStamp) + 1500);
                    }
                   else if(tempPos == 0)
                   {
                       autoPause = true;
                   }
                }
            }else {
                System.out.println("mans playing an add");
                log("an add is playing");
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (SpotifyException e) {
            e.printStackTrace();
        }
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

}
