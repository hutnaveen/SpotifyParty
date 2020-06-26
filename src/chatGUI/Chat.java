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

        this.setLocation(0, 0);
        this.setSize(450, 500000000);
        this.setAutoscrolls(true);
    }

    public void addRequest(JPanel pane)
    {
        pane.setBounds(10, 10 + size++ *85, 430, 80);
        this.add(pane);
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
