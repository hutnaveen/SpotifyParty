package server;

import chatGUI.ChatPanel;
import coroutines.KThreadRepKt;
import handler.MessageHandler;
import lombok.Getter;
import main.SpotifyParty;
import upnp.UPnP;
import utils.NetworkUtils;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import static chatGUI.ChatPanel.names;
import static utils.GUIUtils.resizeIcon;

@Getter
public class SketchServer {
    static DataOutputStream dos;
    static DataInputStream in;
    ServerSocket server;
    private int serverPort = 9000;
    public static HashMap<DataInputStream, DataOutputStream> stream = new HashMap<>();
    public SketchServer()
        {
            try {
                serverPort = findOpenSocket();
                server = new ServerSocket(serverPort);
                ChatPanel.setCode(NetworkUtils.simpleEncode(NetworkUtils.getPublicIP(), serverPort, 0));
                startServerListener();
                System.out.println("Server is started! port " + serverPort);
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        private int findOpenSocket()
        {
            boolean star;
            if(UPnP.isUPnPAvailable())
                System.out.println("gucci");
            else {
                Object[] options = {"OK"};
                JFrame frame = new JFrame();
                frame.setAlwaysOnTop(true);
                JOptionPane.showOptionDialog(frame, "Looks like the internet your connected to does not support port forwarding.\n No worries you can still join a party and host a local party.", "Can't Start Public Party", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, resizeIcon(new ImageIcon(ChatPanel.class.getResource("/images/logo.png")), 50, 50), options, options[0]);
                System.exit(69);
            }
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
            return serverPort;
        }

        public void startServerListener()
        {
            KThreadRepKt.startCor(() -> {
                Socket s = null;
                DataOutputStream dos = null;
                DataInputStream in = null;
                try {
                    s = server.accept();
                    dos = new DataOutputStream((s.getOutputStream()));
                    in = new DataInputStream(new BufferedInputStream(s.getInputStream()));
                    dos.writeUTF(SpotifyParty.api.getOAuthToken().getAccess_token());
                    System.out.println(NetworkUtils.getLocalIP());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("added");
                stream.put(in, dos);
                new ClientListener(in);
            });
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
}
