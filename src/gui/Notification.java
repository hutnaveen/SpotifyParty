package gui;
import utils.Sound;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Notification{
    private String title;
    private String description;
    private String appName;
    private JFrame frame;
    private boolean alive;
    private long timeOut;
    private static Notification prevNotif;
    private JPanel panel;
    private ActionEvent event;
    public Notification(Image icon, String name, String title, String description, long timeOut) {
        System.setProperty("apple.awt.UIElement", "true");
        try {
            UIManager.setLookAndFeel("org.violetlib.aqua.AquaLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        frame = new JFrame();
        frame.getRootPane().putClientProperty("Aqua.windowStyle", "noTitleBar");
        frame.getRootPane().putClientProperty("Aqua.backgroundStyle", "vibrantDark");
        frame.getRootPane().setBackground(new Color(70,70,70));
        frame.setBackground(new Color(70,70,70));
        frame.setResizable(false);
        this.title = title;
        this.appName = name;
        this.timeOut = timeOut;
        this.description = description;
        /*if (title.length() > 29)
            title = title.substring(0, 28) + "...";
        if (description.length() > 35)
            description = description.substring(0, 33) + "...";
        if (appName.length() > 27)
            appName = appName.substring(0, 24) + "...";*/
        String finalTitle = title;
        String finalDescription = description;

                panel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setFont(new Font("", Font.PLAIN, 14));
                        g.setColor(new Color(200, 200, 200));
                        g.drawImage(icon, 10, 10, 17, 17, this);
                        g.drawString(appName, 32, 23);
                        g.setColor(Color.white);
                        g.setFont(new Font("", Font.BOLD, 13));
                        g.drawString(finalTitle, 10, 44);
                        g.setFont(new Font("", Font.PLAIN, 12));
                        g.drawString(finalDescription, 10, 62);
                    }
                };
                panel.setBackground(new Color(50,50,50));
                final int[] x = {0};
                frame.getRootPane().addMouseMotionListener(new MouseAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        super.mouseDragged(e);
                        if (e.getX() > x[0])
                            close();
                    }
                });
                frame.getRootPane().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                        x[0] = e.getX();
                    }
                });
                panel.setOpaque(false);
                int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
                int setWidth = 340;
                frame.setSize(setWidth, 77);
                frame.setLocation(width - (setWidth + 20), 40);
                frame.add(panel);
                frame.setAlwaysOnTop(true);
    }
    public Notification(Image icon, String appName, String title, String description)
    {
        this(icon, appName, title, description, 7000);
    }
    public void addActivationListener(ActionListener listener)
    {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(event == null)
                    exit();
                else {
                    String str = appName + ":" + title + ":" + description;
                    exit();
                    event = new ActionEvent(this, str.hashCode(), str);
                    listener.actionPerformed(event);
                }
            }
        });
    }
    public void send()
    {
        if( prevNotif != null && prevNotif.alive)
            prevNotif.exit();
        frame.setVisible(true);
        alive = true;
        if (timeOut > -1) {
            new Thread(() -> {
                try {
                    Thread.sleep(timeOut);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                close();
            }).start();
        }
        prevNotif = this;
        Sound.playSound(Sound.class.getResource("/sounds/IbizaNotif.wav"));
    }
    public void close()
    {
        /*new Thread(() -> {
            int width = Toolkit.getDefaultToolkit().getScreenSize().width;
            while (alive && frame.getX() < width - 5) {
                frame.setLocation(frame.getX() + 5, frame.getY());
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            frame.dispose();
            alive = false;
        }).start();*/
        exit();
    }
    public void exit()
    {
        frame.dispose();
        alive = false;
    }
}