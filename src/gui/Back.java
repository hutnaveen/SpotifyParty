package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Back extends JPanel {
    CardLayout cl = new CardLayout();
    public Chat chatPanel = new Chat();
    public Requests requestsPanel = new Requests();

    public Back() {
        this.setLayout(cl);
        this.setOpaque(false);
        this.add(chatPanel, "ChatPanel");
        this.add(requestsPanel, "RequestsPanel");

        SpotifyPartyPanelChat.chatPanel.getMode().setActionCommand("Clicked");
        SpotifyPartyPanelChat.chatPanel.getMode().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(SpotifyPartyPanelChat.chatPanel.chatSwitch) {
                    SpotifyPartyPanelChat.chatPanel.req.setText("Song Requests");
                    //cl.show(Back, "RequestPanel"); ERROR
                } else {
                    SpotifyPartyPanelChat.chatPanel.req.setText("Party Chat");
                    //cl.show(Back, "ChatPanel"); ERROR
                }
            }
        });
        cl.show(this, "ChatPanel");
    }
}
