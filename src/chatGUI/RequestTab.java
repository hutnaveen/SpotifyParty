package chatGUI;

import interfaces.SpotifyPlayerAPI;
import spotifyAPI.SpotifyAppleScriptWrapper;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class RequestTab extends JPanel {
    public static SpotifyPlayerAPI api = new SpotifyAppleScriptWrapper();

    public RequestTab(String uri) {
        Border border = BorderFactory.createLineBorder(new Color(40, 40, 40), 1);
        this.setSize(430, 80);
        this.setBackground(Color.BLACK);

        JTextPane song = new JTextPane();
        song.setBorder(border);
        song.setBackground(new Color(40, 40, 40));
        song.setForeground(Color.WHITE);
        song.setEditable(false);
        song.setFont(new Font("Proxima Nova", Font.BOLD, 13));
        StyledDocument doc = song.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        song.setBounds(10, 520, 230, 17);
        this.add(song);

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
