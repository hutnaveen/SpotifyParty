package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Requests extends JPanel {
    public static ArrayList<RequestTab> requestTabs = new ArrayList<>();
    public static JTextPane backText;
    public static int size = 0;
    public static JViewport backViewPort;

    public Requests() {
        this.setLayout(null);
        this.setOpaque(false);
        //putClientProperty("Aqua.backgroundStyle", "vibrantUltraDark");
        backText = new JTextPane();
        backText.setFocusable(false);
        backText.setAutoscrolls(true);
        backText.setOpaque(false);
        backText.setEditable(false);
        backText.requestFocus();
        JScrollPane reqScroll = new JScrollPane();
        reqScroll.putClientProperty("JScrollPane.style", "overlay");
        backViewPort = reqScroll.getViewport();
        reqScroll.getViewport().setView(backText);
        reqScroll.setBounds(0, 0, 450, 450);
        reqScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        reqScroll.setOpaque(false);
        reqScroll.getViewport().setOpaque(false);
        reqScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 517));
        reqScroll.getVerticalScrollBar().setOpaque(false);
        reqScroll.getVerticalScrollBar().setBorder(new EmptyBorder(0, 0, 0, 0));
        reqScroll.getVerticalScrollBar().setUnitIncrement(16);
        reqScroll.getVerticalScrollBar().setBackground(new Color(30, 30, 30));
        reqScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 300));
        reqScroll.setAutoscrolls(true);
        backViewPort.setAutoscrolls(true);
        this.add(reqScroll);
        try {
            UIManager.setLookAndFeel("org.violetlib.aqua.AquaLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

    }
    public static BufferedImage icon;
    static {
        try {
            icon = ImageIO.read(Notification.class.getResource("/images/logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void addRequest(RequestTab pane)
    {
        Notification notif = new Notification(icon, "SpotifyParty", "SONG REQUEST", "a new song request was added", 6000);
        notif.send();
        backViewPort.setViewPosition(new Point(0, Integer.MAX_VALUE/4));
        requestTabs.add(pane);
        pane.setBounds(10, 10 +size++ *110, 430, 110);
        backText.add(pane);
    }

    public static void redraw(String link) {
        backText.removeAll();
        backText.setText("");
        size = 0;
        for(int i = 0; i < requestTabs.size(); i++) {
            if(!(requestTabs.get(i).url.equals(link))) {
                requestTabs.get(i).setBounds(10, 10 + size++ *110, 430, 110);
                backText.setText(backText.getText() + "\n\n\n\n\n\n\n\n\n\n");
                backText.add(requestTabs.get(i));
            } else {
                requestTabs.remove(requestTabs.get(i));
                i--;
            }
        }
    }
}
