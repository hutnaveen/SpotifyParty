package chatGUI;

import interfaces.SpotifyPlayerAPI;
import spotifyAPI.SpotifyAppleScriptWrapper;
import utils.SpotifyUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RequestTab extends JPanel {
    public SpotifyPlayerAPI api = new SpotifyAppleScriptWrapper();
    public String uri;

    public RequestTab(String link) {
        uri = link;
        this.setLayout(null);
        Border border = BorderFactory.createLineBorder(new Color(40, 40, 40), 1);
        this.setSize(430, 80);
        this.setBackground(new Color(40, 40, 40));

        JTextPane song = new JTextPane();
        song.setText("Song");
        song.setBorder(border);
        song.setBackground(new Color(40, 40, 40));
        song.setForeground(Color.WHITE);
        song.setEditable(false);
        song.setFont(new Font("Proxima Nova", Font.BOLD, 16));
        StyledDocument doc = song.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_LEFT);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        song.setBounds(75, 12, 340, 25);
        this.add(song);

        JTextPane artist = new JTextPane();
        //artist.setText(SpotifyUtils.getTrackInfo(uri).getArtist());
        artist.setBorder(border);
        artist.setBackground(new Color(40, 40, 40));
        artist.setForeground(Color.GRAY);
        artist.setEditable(false);
        artist.setFont(new Font("Proxima Nova", Font.BOLD, 16));
        StyledDocument doc3 = artist.getStyledDocument();
        SimpleAttributeSet center3 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center3, StyleConstants.ALIGN_CENTER);
        doc3.setParagraphAttributes(0, doc3.getLength(), center3, false);
        artist.setBounds(75, 42, 340, 25);
        this.add(artist);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                api.playTrack(uri);
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            //g.drawImage(ImageIO.read(getClass().getResource("/SpotifyBG.jpg")), 0, 0, 700, 600, this);
            g.drawImage(ImageIO.read(SpotifyUtils.getTrackInfo(uri).getThumbnailURL()), 10, 10, 60, 60, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
