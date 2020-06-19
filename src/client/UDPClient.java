package client;

import exception.SpotifyException;
import interfaces.SpotifyPlayerAPI;
import spotifyAPI.SpotifyAppleScriptWrapper;

import java.awt.*;
import java.io.File;
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
    private boolean synced = true;
    private String[] storeData;
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
       new Thread(() -> {
           while (true)
           {
               sendToServer("add me");
               try {
                   Thread.sleep(3000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       }).start();
       trackUpdater();
      System.out.println("Client is started! Port: " + clientSocket.getLocalPort());
       try {
           Desktop.getDesktop().open(new File("ClientNotif/SpotifyParty.app"));
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
   private void trackUpdater()
   {
       new Thread(() -> {

           while(true) {
               byte[] receiveData = new byte[1024];
               receivePacket = new DatagramPacket(receiveData, receiveData.length);
               try {
                   clientSocket.receive(receivePacket);
               } catch (IOException e) {
                   e.printStackTrace();
               }
               String[] playerData = new String(receivePacket.getData(), 0, receivePacket.getLength()).split(" ");
               storeData = playerData;
               try {
                   if (!api.isPlaying() && !autoPause)
                       synced = false;
                   else if(api.isPlaying() && !synced)
                   {
                       synced = true;
                       long fact = Long.parseLong(storeData[3].trim());
                       System.out.println((Arrays.toString(storeData)) + " " + new Date(System.currentTimeMillis()) + " " + new Date(fact));
                       long t = Long.parseLong(storeData[2].trim());
                       updatePlayer(storeData[0].trim(),storeData[1].trim().substring(0, 4).startsWith("tru"), t, fact);
                   }else
                   {
                       long fact = Long.parseLong(playerData[3].trim());
                       System.out.println((Arrays.toString(playerData)) + " " + new Date(System.currentTimeMillis()) + " " + new Date(fact));
                       long t = Long.parseLong(playerData[2].trim());
                       updatePlayer(playerData[0].trim(),playerData[1].trim().substring(0, 4).startsWith("tru"), t, fact);
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       }).start();
   }
   private void updatePlayer(String trackID, boolean playing, long pos, long timeStamp)
   {
       try {
           if (!api.getTrackId().contains(":ad:")) {
               if (trackID.equals("ice")) {
                   api.playTrack("ice");
                   try {
                       Desktop.getDesktop().open(new File("SpotifyParty.app"));
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
               else {
                   if ((api.isPlaying() || autoPause || !synced) && !api.getTrackId().equals(trackID)) {
                       api.playTrack(trackID);
                       if (!api.isPlaying())
                           api.play();
                       api.setPlayBackPosition(pos + (System.currentTimeMillis() - timeStamp) + 500);
                   } else if (!playing) {
                       if (api.isPlaying()) {
                           api.pause();
                           autoPause = true;
                       }
                   } else {
                       if (autoPause) {
                           api.play();
                           autoPause = false;
                       }
                   }
                   if (api.isPlaying() && Math.abs(pos - api.getPlayerPosition() + (pos - timeStamp)) >= 3000) {
                       System.out.println("Time: " + pos + " Player: " + api.getPlayerPosition());
                       api.setPlayBackPosition(pos + (System.currentTimeMillis() - timeStamp) + 250);
                   }
               }
           }
       } catch (SpotifyException e) {
           e.printStackTrace();
       }
   }
   public void quit()
   {
       sendToServer("remove me");
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
}
