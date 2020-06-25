
package chatGUI;

import chatGUI.JoinPartyPanel;
import chatGUI.ChatPanel;
import client.TCPClient;
import server.TCPServer;
import utils.NetworkUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class SpotifyPartyPanelChat extends JPanel implements ActionListener {
    public boolean local = false;
    CardLayout cl = new CardLayout();
    public  JoinPartyPanel joinPartyPanel = new JoinPartyPanel();
    public  static ChatPanel chatPanel = new ChatPanel();
    public  SpotifyPartyFrameChat spfc = new SpotifyPartyFrameChat();

    public static boolean host;
    TCPServer server;

    /*public String name;
    public String code;
    private Object TCPServer;*/

    public SpotifyPartyPanelChat() {
        super();
        ChatPanel.code.setText("");
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
            host = false;
            spfc.setVisible(true);
            cl.show(this, "joinPanel");

        }
        else if (e.getActionCommand().equals("hostLocal")) {
            host = true;
            local = true;
            server = new TCPServer(false);
            spfc.setVisible(true);
            cl.show(this, "chatPanel");
        }
        else if(e.getActionCommand().equals("hostPublic")) {
            host = true;
            local = true;
            server = new TCPServer(true);
            spfc.setVisible(true);
            cl.show(this, "chatPanel");
        }
        else if (e.getActionCommand().equals("enterGuest")) {
            String x = JoinPartyPanel.code.getText();
            Object[] code = NetworkUtils.simpleDecode(x);
            if(code != null && available((String)code[0], (int)code[1]))
            {
                ChatPanel.addNames(JoinPartyPanel.name.getText());
                TCPClient cli = new TCPClient((String)code[0], (int)code[1]);
                cl.show(this, "chatPanel");
                chatPanel.updateData();
                ChatPanel.setCode(x);
            } else {
                JoinPartyPanel.two = true;
                JoinPartyPanel.code.setForeground(Color.RED);
                JoinPartyPanel.code.setText("INVALID CODE");
            }

        }
    }
    private static boolean available(String ip, int port) {
        try (Socket ignored = new Socket(ip, port)) {
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

}

