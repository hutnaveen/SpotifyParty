package chatGUI;

import main.SpotifyParty;
import server.TCPServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.HashSet;


public class Chat extends JPanel {
    public static int size = 0;
    private JViewport scroll;
    public static JTextPane back;
    public static HashSet<RequestTab> requestTabs = new HashSet<>();

    public Chat() {
        this.setLayout(null);

        this.setLocation(0, 0);
        this.setAutoscrolls(true);

        back = new JTextPane();
        back.setAutoscrolls(true);
        back.setBackground(Color.GRAY);
        back.setOpaque(false);
        back.setEditable(false);
    }

    public static void addRequest(RequestTab pane)
    {
        if(SpotifyPartyPanelChat.host) {
            TCPServer.sendToClients("request " + pane.getData().getId() + " " + SpotifyPartyPanelChat.FriendName);
        } else {
            try {
                SpotifyPartyPanelChat.cli.getDos().writeUTF("request " + pane.getData().getId() + " " + SpotifyPartyPanelChat.FriendName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        requestTabs.add(pane);
        pane.setBounds(10, 10 + size++ *110, 430, 80);
        back.add(pane);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            g.drawImage(ImageIO.read(getClass().getResource("/SpotifyBG.jpg")), 0, 0, 700, 600, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
