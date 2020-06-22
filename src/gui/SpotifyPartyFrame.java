package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;

public class SpotifyPartyFrame extends JFrame {
    public static TrayIcon trayIcon;
    private SystemTray tray = SystemTray.getSystemTray();
    private PopupMenu menu = new PopupMenu();
    public static MenuItem status = new MenuItem("Welcome!");
    public SpotifyPartyFrame()
    {
        super("SpotifyParty!");
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
        MenuItem open = new MenuItem("Open Connect Panel");
        open.addActionListener(actionEvent -> {
            setVisible(true);
        });
        status.setEnabled(false);
        menu.add(status);
        menu.addSeparator();
        menu.add(open);
        menu.addSeparator();
        menu.add(quit);
        trayIcon = new TrayIcon(image, "SpotifyParty", menu);
        trayIcon.displayMessage(this.getTitle(), "Started ripping " , TrayIcon.MessageType.INFO);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println(e);
        }
    }
    private void initializeFrame()
    {
        setLocation(100, 100);
        setSize(400, 270);
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(this.HIDE_ON_CLOSE);
        setVisible(false);
        setResizable(false);
        setAlwaysOnTop(true);
    }


}
