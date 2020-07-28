package client;

import coroutines.KThreadRepKt;
import handler.MessageHandler;
import main.SpotifyParty;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okio.ByteString;
import spotifyAPI.OSXSpotifyAPI;
import spotifyAPI.SpotifyWebAPI;
import spotifyAPI.WinSpotifyAPI;
import updater.PlayerUpdater;

import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class SketchClient {
    static DataInputStream in;
    static DataOutputStream out;
    static String token;
    static SpotifyWebAPI serverAPI;
    public SketchClient(String ip, int port){
        try {
            Socket socket = new Socket(ip, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
           this.token = in.readUTF();
            System.out.println(token);
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
        KThreadRepKt.startCor(()->{
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

    public static void sendToServer(String msg)
    {
        try {
            out.writeUTF("a " + msg.trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void startUpdater()
    {
        KThreadRepKt.startCor(() -> {
            try {
                new PlayerUpdater(serverAPI.getPlayerData());
                Thread.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
