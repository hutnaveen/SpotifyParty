
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
    public boolean local = false;
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
        spfc.getHostLocal().setActionCommand("hostLocal");
        spfc.getHostLocal().addActionListener(this);
        spfc.getHostPublic().setActionCommand("hostPublic");
        spfc.getHostPublic().addActionListener(this);

        joinPartyPanel.getEnter().setActionCommand("enterGuest");
        joinPartyPanel.getEnter().addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("join")) {
            spfc.setVisible(true);
            cl.show(this, "joinPanel");

        }
        else if (e.getActionCommand().equals("hostLocal")) {
            local = true;
            spfc.setVisible(true);
            cl.show(this, "chatPanel");
        }
        else if(e.getActionCommand().equals("hostPublic")) {
            local = true;
            spfc.setVisible(true);
            cl.show(this, "chatPanel");
        }
        else if (e.getActionCommand().equals("enterGuest")) {
            String name = JoinPartyPanel.name.getText();
            String code = JoinPartyPanel.code.getText();

            // TODO: if the code is valid do this shit
            boolean test = false;
            if(test) {
                cl.show(this, "chatPanel");
            } else {
                JoinPartyPanel.two = true;
                JoinPartyPanel.code.setForeground(Color.RED);
                JoinPartyPanel.code.setText("INVALID CODE");
            }

        }
    }
}

