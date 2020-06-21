package chatGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel {

    public ChatPanel() {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            g.drawImage(ImageIO.read(getClass().getResource("/SpotifyBG.jpg")), 0, 0, 700, 600, this);
            g.drawImage(ImageIO.read(getClass().getResource("/logo.png")), 10, 10, 40, 40, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
