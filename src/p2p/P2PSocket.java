package p2p;
import com.google.gson.Gson;
import coroutines.KThreadRepKt;
import server.ClientInfo;
import upnp.UPnP;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class P2PSocket {
    private ServerSocket server;
    private HashMap<ClientInfo, Socket> peers = new HashMap<>();
    private MessageHandler handler;
    public P2PSocket(InetAddress ip, int port)
    {
        try {
            Socket a = new Socket(ip, port);
            HashSet<ClientInfo> peerAddresses = new Gson().fromJson(new DataInputStream(a.getInputStream()).readUTF(), HashSet.class);
            for(ClientInfo address: peerAddresses)
            {
                Socket socket = new Socket(address.address, address.port);
                peers.put(address, socket);
            }
            peers.put(new ClientInfo(InetAddress.getByName(UPnP.getExternalIP()), 8080), null);
            server = new ServerSocket(8080);
            peerConnectionListener();
        } catch (IOException e) {
            e.printStackTrace();
        }
        peerConnectionListener();
    }
    private P2PSocket(int port)
    {
        try {
            server = new ServerSocket(port);
            peerConnectionListener();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static P2PSocket startNetwork(int port)
    {
        return new P2PSocket(port);
    }
    private void peerConnectionListener()
    {
        KThreadRepKt.startInfCor(() -> {
            try {
                Socket s = server.accept();
                peers.put(new ClientInfo(s.getInetAddress(), s.getPort()), s);
                new DataOutputStream(s.getOutputStream()).writeUTF(new Gson().toJson(peers.keySet()));
                new PeerHandler(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void sendMessage(ClientInfo info, String message)
    {
        try {
            new DataOutputStream(peers.get(info).getOutputStream()).writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendToPeers(String message)
    {
        try {
            for(ClientInfo info: peers.keySet())
                new DataOutputStream(peers.get(info).getOutputStream()).writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Set<ClientInfo> getPeers()
    {
        return peers.keySet();
    }
    class PeerHandler
    {
        private DataInputStream inputStream;
        public PeerHandler(Socket in)
        {
            try {
                inputStream = new DataInputStream(new BufferedInputStream(in.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            KThreadRepKt.startInfCor(() -> {
                try {
                    handler.handle(inputStream.readUTF());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void setHandler(MessageHandler handler) {
        this.handler = handler;
    }
}
