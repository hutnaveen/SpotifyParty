package client;

import chatGUI.ChatPanel;
import com.google.gson.Gson;
import coroutines.KThreadRepKt;
import handler.MessageHandler;
import spotifyAPI.OSXSpotifyAPI;
import spotifyAPI.SpotifyWebAPI;
import spotifyAPI.WinSpotifyAPI;
import updater.PlayerUpdater;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

import static chatGUI.ChatPanel.dat;
import static chatGUI.ChatPanel.names;
import static chatGUI.SpotifyPartyPanelChat.FriendName;
import static main.SpotifyParty.api;

public class SketchClient {
    static DataInputStream in;
    static DataOutputStream out;
    public static String token;
    static SpotifyWebAPI serverAPI;
    public static boolean sync = true;
    public SketchClient(String ip, int port) {
        try {
           Socket socket = new Socket(ip, port);
           in = new DataInputStream(socket.getInputStream());
           out = new DataOutputStream(socket.getOutputStream());
           this.token = in.readUTF();
           dat = api.getUserData();
           out.writeUTF(FriendName.replace(" ", "-") + " " + dat.getImages().get(0).getUrl());
           System.out.println(token);
           names = new Gson().fromJson(in.readUTF(), HashMap.class);
           ChatPanel.addNames();
           if((System.getProperty("os.name").contains("Mac")))
               serverAPI = new OSXSpotifyAPI(token);
           else
               serverAPI = new WinSpotifyAPI(token);
           startMessageListner();
           startUpdater();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startMessageListner()
    {
        KThreadRepKt.startInfCor(()->{
            String dat = null;
            try {
                dat = in.readUTF();
            } catch (EOFException e) {
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int split = dat.indexOf(' ');
            new MessageHandler(dat.substring(0, split), dat.substring(split));
        });
    }

    public static void sendToServer(String msg) {
        try {
            out.writeUTF("a " + msg.trim());
        } catch (SocketException socketException) {
            System.exit(69);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void startUpdater() {
        KThreadRepKt.startInfCor(() -> {
            try {
                if(sync)
                new PlayerUpdater(serverAPI.getPlayerData());
                Thread.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
