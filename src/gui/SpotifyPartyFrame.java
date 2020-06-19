package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;

public class SpotifyPartyFrame extends JFrame {
    private TrayIcon trayIcon;
    private SystemTray tray = SystemTray.getSystemTray();
    private PopupMenu menu = new PopupMenu();
    public static MenuItem status = new MenuItem("Welcome!");
    public SpotifyPartyFrame()
    {
        super();
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
        trayIcon = new TrayIcon(image, "SpotifyParty", menu );
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println(e);
        }
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                setVisible(false);
                setLocation(e.getX()-getWidth()/2, 22);
            }
        });
    }
    private void initializeFrame()
    {
        setLocation(100, 100);
        setSize(400, 250);
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 35, 35));
            }
        });
        setVisible(false);
        setAlwaysOnTop(true);
        setUndecorated(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                //setVisible(false);
            }
        });
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                setVisible(false);
            }
        });
    }


}
