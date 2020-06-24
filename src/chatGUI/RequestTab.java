package chatGUI;

import interfaces.SpotifyPlayerAPI;
import spotifyAPI.SpotifyAppleScriptWrapper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class RequestTab extends JPanel {
    public static SpotifyPlayerAPI api = new SpotifyAppleScriptWrapper();

    public RequestTab(String uri) {
        this.setSize(430, 80);
        this.setBackground(Color.BLACK);



    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            //g.drawImage(ImageIO.read(getClass().getResource("/SpotifyBG.jpg")), 0, 0, 700, 600, this);
            g.drawImage(ImageIO.read(api.getArtworkURL()), 10, 10, 60, 60, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
