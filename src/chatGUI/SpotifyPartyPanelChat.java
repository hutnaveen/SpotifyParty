
package chatGUI;

import chatGUI.JoinPartyPanel;
import chatGUI.ChatPanel;
import server.TCPServer;
import utils.NetworkUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SpotifyPartyPanelChat extends JPanel implements ActionListener{
    CardLayout cl = new CardLayout();
    public static JoinPartyPanel joinPartyPanel = new JoinPartyPanel();
    public static ChatPanel chatPanel = new ChatPanel();
    public static SpotifyPartyFrameChat spfc = new SpotifyPartyFrameChat();
    TCPServer server;
    public SpotifyPartyPanelChat() {
        this.setLayout(cl);
        this.add(joinPartyPanel, "joinPanel");
        this.add(chatPanel, "chatPanel");

        spfc.getJoin().setActionCommand("join");
        spfc.getJoin().addActionListener( this);
        spfc.getHost().setActionCommand("Host");
        spfc.getHost().addActionListener(this);

        cl.show(this, "start");

    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("join")) {
            cl.show(this, );
        }
}
}

