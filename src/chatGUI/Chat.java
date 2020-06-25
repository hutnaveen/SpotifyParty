package chatGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class Chat extends JPanel {
    private int size = 0;
    private JViewport scroll;

    public Chat() {
        this.setLayout(null);

        this.setLocation(250, 0);
        this.setSize(450, 520);

        scroll = new JViewport();
        scroll.setBounds(0, 0, 450, 520);
        scroll.setOpaque(false);
        scroll.setBorder(new EmptyBorder(0,0,0,0));
        this.add(scroll);
    }

    public void addRequest(JPanel pane)
    {
        pane.setBounds(10, 10 + size++ *85, 430, 80);
        scroll.add(pane);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            g.drawImage(ImageIO.read(getClass().getResource("/SpotifyBG.jpg")), 0, 0, 700, 600, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
