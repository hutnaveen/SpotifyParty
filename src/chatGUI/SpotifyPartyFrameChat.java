package chatGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class SpotifyPartyFrameChat extends JFrame {
    public static MenuItem join;
    public static MenuItem hostLocal;
    public static MenuItem hostPublic;
    //public boolean updateAvalibe = true;

    public static TrayIcon  trayIcon = new TrayIcon(new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB), "");
    private  SystemTray tray = SystemTray.getSystemTray();
    private static Image image;

    static {
        try {
            image = ImageIO.read(SpotifyPartyFrameChat.class.getResource("/images/logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PopupMenu menu = new PopupMenu();
    public static MenuItem quit = new MenuItem("Quit");
    public static MenuItem status = new MenuItem("Welcome to Spotify Party!");
    private int WIN = 0;
    public SpotifyPartyFrameChat()
    {
        super();
        setResizable(false);
        setIconImage(image);
        //Taskbar.getTaskbar().setIconImage(image);
        this.getRootPane().putClientProperty("Aqua.windowStyle", "transparentTitleBar");
        this.getRootPane().putClientProperty("Aqua.windowTopMargin", "0");
        this.getRootPane().putClientProperty("Aqua.backgroundStyle", "vibrantUltraDark");
        if(System.getProperty("os.name").contains("Windows"))
        {
            WIN = 10;
            this.getRootPane().setBackground(new Color(30, 30, 30));
        }
        setLocation(100, 100);
        setSize(700+ WIN, 600 + WIN);
        setTitle("SpotifyParty");
        setDefaultCloseOperation(this.HIDE_ON_CLOSE);
        initializeTrayIcon();
        toFront();
        /*
        if(updateAvalibe) {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "A new update to SpotifyParty should" +
                    " be on out website :)", "Update Avalible!", JOptionPane.PLAIN_MESSAGE);
        }

         */
    }
    private void initializeTrayIcon()
    {
        quit.addActionListener(actionEvent -> System.exit(0));

        join = new MenuItem("Join Party");
        hostLocal = new MenuItem("Host Local Party");
        hostPublic = new MenuItem("Host Public Party");

        status.setEnabled(false);
        menu.add(status);
        menu.addSeparator();
        menu.add(join);
        menu.addSeparator();
        //menu.add(hostLocal);
        //menu.addSeparator();
        menu.add(hostPublic);
     //   hostPublic.addActionListener();
        menu.addSeparator();


        menu.add(quit);
        try {
            trayIcon = new TrayIcon(image, "SpotifyParty", menu );
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println(e);
        }
    }

    //public MenuItem getJoin() {return join;}
    //public MenuItem getHostLocal() {return hostLocal;}
    //public MenuItem getHostPublic() {return hostPublic;}


}