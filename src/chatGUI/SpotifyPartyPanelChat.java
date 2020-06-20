
package chatGUI;

import chatGUI.JoinPartyPanel;
import chatGUI.ChatPanel;
import server.TCPServer;
import utils.NetworkUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SpotifyPartyPanelChat extends JPanel implements ActionListener {
    CardLayout cl = new CardLayout();
    public  JoinPartyPanel joinPartyPanel = new JoinPartyPanel();
    public  ChatPanel chatPanel = new ChatPanel();
    public  SpotifyPartyFrameChat spfc = new SpotifyPartyFrameChat();
    TCPServer server;

    public String name;
    public String code;

    public SpotifyPartyPanelChat() {
        super();

        this.setLayout(cl);
        this.add(joinPartyPanel, "joinPanel");
        this.add(chatPanel, "chatPanel");
        spfc.add(this);

        spfc.getJoin().setActionCommand("join");
        spfc.getJoin().addActionListener(this);
        spfc.getHost().setActionCommand("host");
        spfc.getHost().addActionListener(this);

        joinPartyPanel.getEnter().setActionCommand("enterGuest");
        joinPartyPanel.getEnter().addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("join")) {
            spfc.setVisible(true);
            cl.show(this, "joinPanel");

        }
        else if (e.getActionCommand().equals("host")) {
            spfc.setVisible(true);
            cl.show(this, "chatPanel");

        }
        else if (e.getActionCommand().equals("enterGuest")) {
            cl.show(this, "chatPanel");

        }
    }
}

