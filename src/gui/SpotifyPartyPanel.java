package gui;
import server.UDPServer;
import utils.NetworkUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SpotifyPartyPanel extends JPanel implements ActionListener{
    CardLayout cl = new CardLayout();
    StartingPanel start = new StartingPanel();
    public static GuestPanel guest = new GuestPanel();
    public static HostPanel host = new HostPanel();
    UDPServer server;
    Thread starter;
    public SpotifyPartyPanel() {

        this.setSize(400, 400);
        this.setLayout(cl);

        start.getHost().setActionCommand("h");
        start.getHost().addActionListener(this);
        start.getJoin().setActionCommand("j");
        start.getJoin().addActionListener(this);

        guest.getBack().setActionCommand("k");
        guest.getBack().addActionListener(this);

        host.getBack().setActionCommand("i");
        host.getBack().addActionListener(this);

        this.add(start, "start");
        this.add(guest, "guest");
        this.add(host, "host");
        cl.show(this, "start");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("h"))
        {
            cl.show(this, "guest");
            return;
        }
        else if(e.getActionCommand().equals("j"))
        {
            cl.show(this, "host");
            starter = new Thread(() -> {
                int port = 9005;
                String code = NetworkUtils.simpleEncode(NetworkUtils.getPublicIP().trim(), port,0);
                host.setCode(code);
                server = new UDPServer(port, true);
            });
            starter.start();

            return;
        }
        else if(e.getActionCommand().equals("k")) {
            cl.show(this, "start");
        }
        else if(e.getActionCommand().equals("i")) {
            cl.show(this, "start");
            if(starter != null)
            {
                starter.stop();
                starter = null;
            }
            if(server != null)
            {
                server.quit();
                server = null;
            }
        }
    }
}
