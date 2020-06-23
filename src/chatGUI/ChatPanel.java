package chatGUI;

import exception.SpotifyException;
import interfaces.SpotifyPlayerAPI;
import model.TrackInfo;
import spotifyAPI.SpotifyAppleScriptWrapper;
import utils.SpotifyUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static gui.GUIUtil.makeButton;
import static gui.GUIUtil.resizeIcon;

public class ChatPanel extends JPanel {
    public static SpotifyPlayerAPI api = new SpotifyAppleScriptWrapper();
    public static JTextPane area;
    public static JTextPane song;
    public static JTextPane artist;
    public static RoundJTextField code;
    public static ArrayList<String> names = new ArrayList<>();
    public static AbstractButton copy;

    public static RoundJTextField type;
    private URL artworkURL;

    public ChatPanel() {
        this.setLayout(null);
        Border border = BorderFactory.createLineBorder(new Color(40, 40, 40), 1);

        code = new RoundJTextField(200);
        code.setForeground(Color.GRAY);
        code.setBounds(40, 10, 195, 30);
        code.setEditable(false);
        code.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                System.out.println("Here");
                StringSelection selection = new StringSelection(code.getText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            }
        });
        this.add(code);

        JLabel text = new JLabel("Friends", SwingConstants.CENTER);
        text.setFont(new Font("Proxima Nova", Font.BOLD, 30));
        text.setForeground(Color.WHITE);
        text.setBounds(-70, 20, 400, 100);
        this.add(text);

        song = new JTextPane();
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

        artist = new JTextPane();
        artist.setBorder(border);
        artist.setBackground(new Color(40, 40, 40));
        artist.setForeground(Color.GRAY);
        artist.setEditable(false);
        artist.setFont(new Font("Proxima Nova", Font.BOLD, 13));
        StyledDocument doc2 = artist.getStyledDocument();
        SimpleAttributeSet center2 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center2, StyleConstants.ALIGN_CENTER);
        doc2.setParagraphAttributes(0, doc2.getLength(), center2, false);
        artist.setBounds(10, 540, 230, 17);
        this.add(artist);


        area = new JTextPane();
        area.setBorder(border);
        area.setAutoscrolls(true);
        area.setEditable(false);
        area.setForeground(Color.WHITE);
        area.setFont(new Font("Proxima Nova", Font.BOLD, 15));
        StyledDocument doc3 = area.getStyledDocument();
        SimpleAttributeSet center3 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center3, StyleConstants.ALIGN_CENTER);
        doc3.setParagraphAttributes(0, doc3.getLength(), center3, false);
        area.setBackground(new Color(40, 40, 40));
        JScrollPane areaScroll = new JScrollPane(area);
        areaScroll.setBorder(border);
        areaScroll.setBackground(new Color(40, 40, 40));
        areaScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 300));
        areaScroll.setBounds(25, 110, 200, 250);
        this.add(areaScroll);

        type = new RoundJTextField(380);
        type.setBounds(260, 530, 380, 40);
        this.add(type);

        Chat chat  = new Chat();
        this.add(chat);
    }

    public static void addNames(String name) {
        names.add(name);
        String str = "";
        for(int i = 0; i < names.size(); i++) {
            str = str + (" " + names.get(i) + "\n\n");
        }
        area.setText(str);
    }


    public void updateData(String trackID)
    {
        TrackInfo inf = SpotifyUtils.getTrackInfo(trackID);
        artworkURL = inf.getThumbnailURL();
        song.setText(inf.getName());
        artist.setText(api.getTrackArtist());
        repaint();
    }

    public void updateData()
    {
        TrackInfo inf = SpotifyUtils.getTrackInfo(api.getTrackId());
        artworkURL = inf.getThumbnailURL();
        song.setText(inf.getName());
        artist.setText(api.getTrackArtist());
        repaint();
    }
    public static void setCode(String tcode)
    {
        code.setFont(new Font("Proxima Nova", Font.PLAIN, 11));
        code.setText(tcode);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            g.drawImage(ImageIO.read(getClass().getResource("/SpotifyBG.jpg")), 0, 0, 700, 600, this);
            g.setColor(new Color(40, 40, 40));
            g.fillRect(0, 0, 250, 600);
            g.drawImage(ImageIO.read(getClass().getResource("/logo.png")), 10, 15, 20, 20, this);
            g.setColor(Color.WHITE);
            if(artworkURL != null)
                g.drawImage(ImageIO.read(artworkURL), 70, 390, 115, 115, this);
            //g.drawLine(30, 90, 220, 90);
            //g.drawLine(30, 370, 220, 370);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
