
package chatGUI;

import client.SketchClient;
import gui.JoinPartyPanel;
import server.SketchServer;
import utils.NetworkUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static chatGUI.SpotifyPartyFrameChat.menu;
import static chatGUI.SpotifyPartyFrameChat.quit;
import static main.SpotifyParty.chatPanel;
import static main.SpotifyParty.api;
import static utils.GUIUtils.resizeIcon;

public class SpotifyPartyPanelChat extends JPanel implements ActionListener {
    public boolean local = false;
    CardLayout cl = new CardLayout();
    public JoinPartyPanel joinPartyPanel = new JoinPartyPanel();
    public static SpotifyPartyFrameChat spfc = new SpotifyPartyFrameChat();
    public static String FriendName = "";
    public static boolean host;
    SketchServer server;
    public static SketchClient cli ;

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
            Object[] options = { "OK"};
            JFrame frame = new JFrame();
            frame.setAlwaysOnTop(true);
            JOptionPane.showOptionDialog(frame, "Your party will start shortly!", "Spotify Party!",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    resizeIcon(new ImageIcon(ChatPanel.class.getResource("/images/logo.png")), 50, 50), options, options[0]);

            FriendName = api.getUserData().getDisplay_name();
            FriendName = FriendName.replace(" ", "-");
            host = true;
            local = true;
            server = new SketchServer();
            cl.show(this, "chatPanel");
            spfc.setVisible(true);
            menu.removeAll();
            menu.add(show);
            menu.addSeparator();
            menu.add(quit);
            chatPanel.updateData();

        }
        else if (e.getActionCommand().equals("enterGuest")) {
            FriendName = api.getUserData().getDisplay_name();
            String x = JoinPartyPanel.code.getText();
            Object[] code = NetworkUtils.simpleDecode(x);
            if(code != null)
            {
                System.out.println(x);
                FriendName = api.getUserData().getDisplay_name();
                FriendName = FriendName.replace(" ", "-");
                cli =  new SketchClient((String)code[0], (int)code[1]);
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

