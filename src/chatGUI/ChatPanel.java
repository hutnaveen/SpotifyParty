package chatGUI;

import exception.SpotifyException;
import interfaces.SpotifyPlayerAPI;
import spotifyAPI.SpotifyAppleScriptWrapper;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

import static gui.GUIUtil.makeButton;
import static gui.GUIUtil.resizeIcon;

public class ChatPanel extends JPanel {
    private SpotifyPlayerAPI api = new SpotifyAppleScriptWrapper();
    public static ArrayList<String> names = new ArrayList<>();
    public static AbstractButton copy;

    public ChatPanel() {
        names.add("Bella Hadid");
        names.add("Travis");
        names.add("Hrithik");
        names.add("Ritvik");
        names.add("The Weeknd");
        names.add("XXX");
        names.add("Jay Sean");
        names.add("JCole");
        names.add("Kendrick");
        names.add("Tyle, the creator");
        names.add("Juice Wrld");
        names.add("Emma Watson");
        names.add("Jennifer Aniston");
        names.add("Monica");
        this.setLayout(null);

        JTextField code = new RoundJTextField(200);
        code.setForeground(Color.GRAY);
        code.setText("Code");
        code.setBounds(40, 10, 195, 30);
        code.setEditable(false);
        this.add(code);

        JLabel text = new JLabel("Friends", SwingConstants.CENTER);
        text.setFont(new Font("Proxima Nova", Font.BOLD, 30));
        text.setForeground(Color.WHITE);
        text.setBounds(-70, 20, 400, 100);
        this.add(text);

        JTextPane song = new JTextPane();
        song.setBackground(Color.BLACK);
        song.setForeground(Color.WHITE);
        song.setEditable(false);
        song.setFont(new Font("Proxima Nova", Font.BOLD, 13));
        song.setText(getSong());
        StyledDocument doc = song.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        song.setBounds(10, 520, 230, 17);
        this.add(song);

        JTextPane artist = new JTextPane();
        artist.setBackground(Color.BLACK);
        artist.setForeground(Color.GRAY);
        artist.setEditable(false);
        artist.setFont(new Font("Proxima Nova", Font.BOLD, 13));
        artist.setText(getArtist());
        StyledDocument doc2 = artist.getStyledDocument();
        SimpleAttributeSet center2 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center2, StyleConstants.ALIGN_CENTER);
        doc2.setParagraphAttributes(0, doc2.getLength(), center2, false);
        artist.setBounds(10, 540, 230, 17);
        this.add(artist);


        JTextPane area = new JTextPane();
        area.setAutoscrolls(true);
        area.setEditable(false);
        area.setForeground(Color.WHITE);
        area.setFont(new Font("Proxima Nova", Font.BOLD, 13));
        area.setText(getNames());
        StyledDocument doc3 = area.getStyledDocument();
        SimpleAttributeSet center3 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center3, StyleConstants.ALIGN_CENTER);
        doc3.setParagraphAttributes(0, doc3.getLength(), center3, false);
        area.setBackground(Color.BLACK);
        JScrollPane areaScroll = new JScrollPane(area);
        areaScroll.setBackground(Color.BLACK);
        areaScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 300));
        areaScroll.setBounds(25, 100, 200, 280);
        this.add(areaScroll);
    }

    public String getNames() {
        String str = "";
        for(int i = 0; i < names.size(); i++) {
            str = str + (" " + names.get(i) + "\n\n");
        }
        return str;
    }

    public URL getAlbumCover() throws SpotifyException {
        return api.getArtworkURL();
    }

    public String getSong() {
        return api.getTrackName();
    }

    public String getArtist() {
        return api.getTrackArtist();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            g.drawImage(ImageIO.read(getClass().getResource("/SpotifyBG.jpg")), 0, 0, 700, 600, this);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 250, 600);
            g.drawImage(ImageIO.read(getClass().getResource("/logo.png")), 10, 15, 20, 20, this);
            g.setColor(Color.WHITE);
            g.drawLine(30, 90, 220, 90);
            g.drawImage(ImageIO.read(getAlbumCover()), 70, 390, 115, 115, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
