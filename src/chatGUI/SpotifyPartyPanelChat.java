
package chatGUI;

import client.TCPClient;
import gui.JoinPartyPanel;
import server.TCPServer;
import spotifyAPI.OSXSpotifyAPI;
import utils.NetworkUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static chatGUI.SpotifyPartyFrameChat.menu;
import static chatGUI.SpotifyPartyFrameChat.quit;
import static main.SpotifyParty.chatPanel;
import static main.SpotifyParty.api;

public class SpotifyPartyPanelChat extends JPanel implements ActionListener {
    public boolean local = false;
    CardLayout cl = new CardLayout();
    public JoinPartyPanel joinPartyPanel = new JoinPartyPanel();
    public static SpotifyPartyFrameChat spfc = new SpotifyPartyFrameChat();
    public static String FriendName = "";
    public static boolean host;
    TCPServer server;
    public static TCPClient cli ;

    public SpotifyPartyPanelChat() {
        super();
        ChatPanel.code.setText("");
        this.setLayout(cl);
        this.add(joinPartyPanel, "joinPanel");
        this.add(chatPanel, "chatPanel");
        spfc.add(this);

        SpotifyPartyFrameChat.join.setActionCommand("join");
        SpotifyPartyFrameChat.join.addActionListener(this);
        SpotifyPartyFrameChat.hostPublic.setActionCommand("hostPublic");
        SpotifyPartyFrameChat.hostPublic.addActionListener(this);
        show.addActionListener(actionEvent -> spfc.setVisible(true));
        joinPartyPanel.getEnter().setActionCommand("enterGuest");
        joinPartyPanel.getEnter().addActionListener(this);
     //   spfc.setVisible(true);
        cl.show(this, "signUp");
    }
    MenuItem show = new MenuItem("Show Window");
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("join")) {
            host = false;
            spfc.setVisible(true);
            cl.show(this, "joinPanel");
        }
        /*
        else if (e.getActionCommand().equals("hostLocal")) {
            FriendName = "Host";
            host = true;
            local = true;
            server = new TCPServer(false);
            spfc.setVisible(true);
            cl.show(this, "chatPanel");
            menu.removeAll();
            menu.add(show);
            menu.addSeparator();
            menu.add(quit);
            chatPanel.updateData();

        }

         */
        else if(e.getActionCommand().equals("hostPublic")) {
            FriendName = "Host";
            host = true;
            local = true;
            server = new TCPServer(true);
            cl.show(this, "chatPanel");
            spfc.setVisible(true);
            menu.removeAll();
            menu.add(show);
            menu.addSeparator();
            menu.add(quit);
            chatPanel.updateData();

        }
        else if (e.getActionCommand().equals("enterGuest")) {
            String x = JoinPartyPanel.code.getText();
            Object[] code = NetworkUtils.simpleDecode(x);
            if(code != null)
            {
                System.out.println(x);
                FriendName = JoinPartyPanel.name.getText();
                FriendName = FriendName.replace(" ", "-");
                cli =  new TCPClient((String)code[0], (int)code[1]);
                cl.show(this, "chatPanel");
                chatPanel.updateData();
                ChatPanel.theCode[0] = x;
                ChatPanel.setCode(x);
                menu.removeAll();
                menu.add(show);
                menu.addSeparator();
                menu.add(quit);
            } else {
                JoinPartyPanel.two = true;
                JoinPartyPanel.code.setForeground(Color.RED);
                JoinPartyPanel.code.setText("INVALID CODE");
            }
        }
    }
}

