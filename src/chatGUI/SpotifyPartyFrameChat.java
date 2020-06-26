package chatGUI;

import org.violetlib.aqua.AquaRootPaneUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class SpotifyPartyFrameChat extends JFrame {
    public MenuItem join;
    public MenuItem hostLocal;
    public MenuItem hostPublic;

    public static TrayIcon  trayIcon = new TrayIcon(new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB), "");
    private  SystemTray tray = SystemTray.getSystemTray();
    private static Image image;

    static {
        try {
            image = ImageIO.read(SpotifyPartyFrameChat.class.getResource("/logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static PopupMenu menu = new PopupMenu();
    public static MenuItem status = new MenuItem("Welcome to Spotify Party!");
    public SpotifyPartyFrameChat()
    {
        super();
        getRootPane().putClientProperty("Aqua.windowStyle", "transparentTitleBar");
        if(SpotifyPartyChat.darkMode)
            getRootPane().putClientProperty("Aqua.backgroundStyle", "vibrantUltraDark");
        else
            getRootPane().putClientProperty("Aqua.backgroundStyle", "vibrantUnderWindowBackground");
        getRootPane().putClientProperty("Aqua.windowTopMargin", "600");
        toFront();
        initializeFrame();
        initializeTrayIcon();
    }
    private void initializeTrayIcon()
    {

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
        try {
            trayIcon = new TrayIcon(image, "SpotifyParty", menu );
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
        setUndecorated(true);
        setBackground(new Color(1.0f,1.0f,1.0f,0.5f));
        setLocation(100, 100);
        setSize(700, 600);
        setResizable(false);
        setDefaultCloseOperation(this.HIDE_ON_CLOSE);
    }


}