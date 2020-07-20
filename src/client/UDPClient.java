package client;

import exception.SpotifyException;
import interfaces.SpotifyPlayerAPI;
import spotifyAPI.SpotifyAppleScriptWrapper;
import utils.TimeUtils;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Date;


public class UDPClient {
    private DatagramPacket receivePacket = null;
    private InetAddress IPAddress;
    private DatagramSocket clientSocket;
    private SpotifyPlayerAPI api;
    private final int serverPort;
    private boolean autoPause = false;
    private Thread updater;
    private Thread tempUpdate;
    public UDPClient(String serverIP, int serverPort, int clientSocketNum)
   {
       api = new SpotifyAppleScriptWrapper();
      // api.play();
       this.serverPort = serverPort;
       try {
           IPAddress = InetAddress.getByName(serverIP);
       } catch (UnknownHostException e) {
           // TODO: 6/12/20 can't connect to server
           e.printStackTrace();
       }
       try {
           clientSocket = new DatagramSocket(clientSocketNum);
       } catch (SocketException e) {
           // TODO: 6/12/20 Port Number is taken
           e.printStackTrace();
       }
       sendToServer("add me");
       trackUpdater();
      System.out.println("Client is started! Port: " + clientSocket.getLocalPort());
   }
    public void quit()
    {
        updater.stop();
        tempUpdate.stop();
    }
   private void trackUpdater()
   {
       updater = new Thread(() -> {

           while(true) {
               byte[] receiveData = new byte[1024];
               receivePacket = new DatagramPacket(receiveData, receiveData.length);
               try {
                   clientSocket.receive(receivePacket);
               } catch (IOException e) {
                   e.printStackTrace();
               }
               String[] playerData = new String(receivePacket.getData(), 0, receivePacket.getLength()).split(" ");
               try {
                   long fact = Long.parseLong(playerData[3].trim());
                   System.out.println((Arrays.toString(playerData)) + " " + new Date(TimeUtils.getAppleTime()) + " " + new Date(fact));
                   log((Arrays.toString(playerData)) + " " + new Date(TimeUtils.getAppleTime()) + " " + new Date(fact));
                   long t = Long.parseLong(playerData[2].trim());
                   String[] finalPlayerData = playerData;
                   if(tempUpdate != null) {
                       tempUpdate.stop();
                       tempUpdate = new Thread(() -> updatePlayer(finalPlayerData[0].trim(), finalPlayerData[1].trim().substring(0, 4).startsWith("tru"), t, fact));
                       tempUpdate.start();
                   }else
                   {
                       tempUpdate = new Thread(() -> updatePlayer(finalPlayerData[0].trim(), finalPlayerData[1].trim().substring(0, 4).startsWith("tru"), t, fact));
                       tempUpdate.start();
                   }

               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       });
       updater.start();
   }
    private void updatePlayer(String trackID, boolean playing, long pos, long timeStamp) {
        try {
            String tempTrack = api.getTrackUri();
            boolean tempPlaying = api.isPlaying();
            log(""+tempPlaying);
            long tempPos = api.getPlayBackPosition();
            if (!tempTrack.contains(":ad:")) {
                if (trackID.equals("ice")) {
                    api.pause();
                    autoPause = true;
                } else {

                    if ((tempPlaying || autoPause) && !tempTrack.equals(trackID)) {
                        api.playTrack(trackID);
                        if (!tempPlaying)
                            api.play();
                        System.out.println(pos + (TimeUtils.getAppleTime() - timeStamp) + 2000);
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
                    if (tempPlaying && Math.abs((TimeUtils.getAppleTime() - timeStamp) + pos - tempPos) > 2000) {
                        System.out.println("Time: " + pos + " Player: " + tempPos);
                        api.setPlayBackPosition(pos + (TimeUtils.getAppleTime() - timeStamp) + 1500);
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
   private void sendToServer(String msg)
   {
       byte[] sendData = msg.getBytes();
       DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort);
       try {
           clientSocket.send(sendPacket);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
    private boolean log(String msg)
    {
        return true;
    }
}
