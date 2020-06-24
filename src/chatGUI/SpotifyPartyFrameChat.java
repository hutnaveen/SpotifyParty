package chatGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;

public class SpotifyPartyFrameChat extends JFrame {
    public MenuItem join;
    public MenuItem hostLocal;
    public MenuItem hostPublic;

    private TrayIcon trayIcon;
    private SystemTray tray = SystemTray.getSystemTray();
    private PopupMenu menu = new PopupMenu();
    public static MenuItem status = new MenuItem("Welcome to Spotify Party!");
    public SpotifyPartyFrameChat()
    {
        super();
        toFront();
        initializeFrame();
        initializeTrayIcon();
    }
    private void initializeTrayIcon()
    {
        Image image = null;
        try {
            image = ImageIO.read(getClass().getResource("/logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MenuItem quit = new MenuItem("Quit");
        quit.addActionListener(actionEvent -> System.exit(0));
        join = new MenuItem("Join Party");
        hostLocal = new MenuItem("Host Local Party");
        hostPublic = new MenuItem("Host Public Party");

        status.setEnabled(false);
        menu.add(status);
        menu.addSeparator();
        menu.add(join);
        menu.addSeparator();
        menu.add(hostLocal);
        menu.addSeparator();
        menu.add(hostPublic);
        menu.addSeparator();
        menu.add(quit);

        trayIcon = new TrayIcon(image, "SpotifyParty", menu );
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println(e);
        }
    }

    public MenuItem getJoin() {return join;}
    public MenuItem getHostLocal() {return hostLocal;}
    public MenuItem getHostPublic() {return hostPublic;}

    private void initializeFrame()
    {
        setLocation(100, 100);
        setSize(700, 600);
        setResizable(false);
        setDefaultCloseOperation(this.HIDE_ON_CLOSE);
    }


}