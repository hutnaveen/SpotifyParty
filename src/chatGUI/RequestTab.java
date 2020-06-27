package chatGUI;

import interfaces.SpotifyPlayerAPI;
import model.TrackInfo;
import spotifyAPI.SpotifyAppleScriptWrapper;
import utils.SpotifyUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import static gui.GUIUtil.makeButton;
import static gui.GUIUtil.resizeIcon;

public class RequestTab extends JPanel {
    public SpotifyPlayerAPI api = new SpotifyAppleScriptWrapper();
    public String uri;
    private TrackInfo info;

    public JTextPane song;
    public JTextPane artist;

    public RequestTab(String link) {
        uri = link;
        this.info = SpotifyUtils.getTrackInfo(uri);
        this.setLayout(null);
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setOpaque(false);
        this.setSize(430, 90);
        this.setBackground(new Color(40, 40, 40));

        PopupMenu menu = new PopupMenu();
        MenuItem like = new MenuItem("like");

        menu.add(like);
        MenuItem delete = new MenuItem("delete");
        if(SpotifyPartyPanelChat.host) {
            MenuItem play = new MenuItem("play");
            menu.add(delete);
            menu.add(play);
        }

        ImageIcon playIcon = resizeIcon(new ImageIcon(getClass().getResource("/3dots.png")), 23, 6);
        JLabel opt = new JLabel(playIcon);
        this.add(opt);
        this.add(menu);
        opt.setBounds(380, 37, 30, 6);
        opt.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        song = new JTextPane();
        song.setOpaque(false);
        song.setText(info.getName());
        song.setBorder(new EmptyBorder(0,0,0,0));
        song.setForeground(Color.WHITE);
        song.setEditable(false);
        song.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 17));
        animate(song);
        StyledDocument doc = song.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_LEFT);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        song.setBounds(80, 15, 275, 25);
        this.add(song);

        artist = new JTextPane();
        artist.setOpaque(false);
        artist.setText(info.getArtist());
        artist.setBorder(new EmptyBorder(0,0,0,0));
        artist.setForeground(Color.GRAY);
        artist.setEditable(false);
        artist.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 14));
        animate(artist);
        StyledDocument doc3 = artist.getStyledDocument();
        SimpleAttributeSet center3 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center3, StyleConstants.ALIGN_LEFT);
        doc3.setParagraphAttributes(0, doc3.getLength(), center3, false);
        artist.setBounds(80, 39, 275, 25);
        this.add(artist);

        animate(this);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if(SpotifyPartyPanelChat.host) {
                    api.playTrack(uri);
                }
            }
        });
    }

    public void animate(JComponent obj) {
        obj.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                song.setForeground(Color.GREEN);
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                song.setForeground(Color.WHITE);
            }
        });
    }

    public TrackInfo getData() {
        return this.info;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            Color color = info.getDominantColor();
            Graphics2D g2d = (Graphics2D) g;
            Color color1 = color.darker();
            Color color2 = color.darker().darker();
            GradientPaint gp = new GradientPaint(
                    0, 0, color2, 0, 90, color1);
            g2d.setPaint(gp);
            g2d.fillRoundRect(0, 0, 430, 80, 20, 20);
            //g.drawImage(ImageIO.read(getClass().getResource("/SpotifyBG.jpg")), 0, 0, 700, 600, this);
            g.drawImage(ImageIO.read(SpotifyUtils.getTrackInfo(uri).getThumbnailURL()), 10, 9, 60, 60, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
