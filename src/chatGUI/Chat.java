package chatGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class Chat extends JPanel {
    public static int size = 0;
    private JViewport scroll;
    public static JTextPane back;

    public Chat() {
        this.setLayout(null);

        this.setLocation(0, 0);
        this.setAutoscrolls(true);

        back = new JTextPane();
        back.setAutoscrolls(true);
        back.setBackground(Color.GRAY);
        back.setOpaque(false);
        back.setEditable(false);
    }

    public static void addRequest(JPanel pane)
    {
        pane.setBounds(10, 10 + size++ *90, 430, 80);
        back.add(pane);
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
