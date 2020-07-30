package chatGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.desktop.AppReopenedEvent;
import java.awt.desktop.AppReopenedListener;
import java.awt.desktop.SystemEventListener;
import java.awt.desktop.SystemSleepListener;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static chatGUI.SpotifyPartyPanelChat.spfc;


public class SpotifyPartyFrameChat extends JFrame {
    public static MenuItem join;
    public static MenuItem hostLocal;
    public static MenuItem hostPublic;
    //public boolean updateAvalibe = true;

    //public static TrayIcon trayIcon = new TrayIcon(new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB), "");
    //private  SystemTray tray = SystemTray.getSystemTray();
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
            getRootPane().putClientProperty("Aqua.windowStyle", "transparentTitleBar");
            getRootPane().putClientProperty("Aqua.windowTopMargin", "0");
            getRootPane().putClientProperty("Aqua.backgroundStyle", "vibrantUltraDark");
            if(System.getProperty("os.name").contains("Windows"))
            {
                WIN = 10;
                getRootPane().setBackground(new Color(30, 30, 30));
            }
            setLocation(100, 100);
            setSize(700+ WIN, 600 + WIN);
            if(System.getProperty("os.name").contains("Windows"))
                setTitle("SpotifyParty");
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            initializeTrayIcon();
            setAlwaysOnTop(true);
            toFront();
            setAlwaysOnTop(false);
        java.awt.Desktop.getDesktop().addAppEventListener(new AppReopenedListener() {
            @Override
            public void appReopened(AppReopenedEvent e) {
                setVisible(true);
            }
        });
    }
    public static PopupMenu cmenu = new PopupMenu();
    private void initializeTrayIcon()
    {
        quit.addActionListener(actionEvent -> System.exit(0));

        join = new MenuItem("Join Party");
        hostLocal = new MenuItem("Host Local Party"); 
        hostPublic = new MenuItem("Host Public Party");

        status.setEnabled(false);
       /* menu.add(status);
        menu.addSeparator();
        menu.add(join);
        menu.addSeparator();
        //menu.add(hostLocal);
        //menu.addSeparator();
        menu.add(hostPublic);
     //   hostPublic.addActionListener();
        menu.addSeparator();


        menu.add(quit);*/
        //trayIcon = new TrayIcon(image, "SpotifyParty", menu );
        cmenu.add(status);
        cmenu.add(hostPublic);
        cmenu.add(join);
        Taskbar.getTaskbar().setMenu(cmenu);
        //tray.add(trayIcon);
    }

    //public MenuItem getJoin() {return join;}
    //public MenuItem getHostLocal() {return hostLocal;}
    //public MenuItem getHostPublic() {return hostPublic;}


}