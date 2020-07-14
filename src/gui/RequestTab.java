package gui;

import interfaces.SpotifyPlayerAPI;
import model.Artist;
import model.Track;
import spotifyAPI.SpotifyAppleScriptWrapper;
import utils.SpotifyUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static gui.GUIUtilsChat.resizeIcon;


public class RequestTab extends JPanel {
    public SpotifyPlayerAPI api = new SpotifyAppleScriptWrapper();
    public String url;

    private Track info;

    public JTextPane song;
    public JTextPane artist;

    public String name;

    public RequestTab(String link, String str) {
        info = SpotifyUtils.getTrack(link);
            url = link;
            name = str;
            this.setLayout(null);
            this.setBorder(new EmptyBorder(0, 0, 0, 0));
            this.setOpaque(false);
            this.setSize(430, 110);
            this.setBackground(new Color(40, 40, 40));

            JLabel nameLabel = new JLabel(name);
            nameLabel.setBounds(0, 9, 150, 18);
            nameLabel.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 17));
            nameLabel.setOpaque(false);
            nameLabel.setForeground(Color.WHITE);
            this.add(nameLabel);

            PopupMenu menu = new PopupMenu();
            if (!SpotifyPartyPanelChat.host) {
                MenuItem like = new MenuItem("Vote");
                menu.add(like);
            }

            if (SpotifyPartyPanelChat.host) {
                MenuItem play = new MenuItem("Play");
                System.out.println(info.getUri());
                play.addActionListener(e -> api.playTrack(info.getUri()));
                menu.add(play);

                MenuItem delete = new MenuItem("Delete");
                delete.addActionListener(e -> {
                    Requests.redraw(url);
                });
                menu.add(delete);
            }

            ImageIcon playIcon = resizeIcon(new ImageIcon(getClass().getResource("/images/3dots.png")), 23, 6);
            JLabel opt = new JLabel(playIcon);
            this.add(opt);
            this.add(menu);
            opt.setBounds(380, 67, 30, 6);
            opt.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            });

            song = new JTextPane();
            song.setFocusable(false);
            song.setOpaque(false);
            song.setText(info.getName());
            song.setBorder(new EmptyBorder(0, 0, 0, 0));
            song.setForeground(Color.WHITE);
            song.setEditable(false);
            song.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 17));
            animate(song);
            StyledDocument doc = song.getStyledDocument();
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_LEFT);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
            song.setBounds(80, 45, 275, 25);
            this.add(song);

            artist = new JTextPane();
            artist.setFocusable(false);
            artist.setOpaque(false);
            StringBuilder artists = new StringBuilder();
            for (Artist art : info.getArtists()) {
                artists.append(art.getName() + ", ");
            }
        /*
        artists.replace(artists.length()-2, artists.length(), "");
        if(artists.length() > 26) {
            artists.delete(26, artists.length());
            artists.append("...");
        }

         */
            artist.setText(artists.toString());
            artist.setBorder(new EmptyBorder(0, 0, 0, 0));
            artist.setForeground(Color.GRAY);
            artist.setEditable(false);
            artist.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 14));
            animate(artist);
            StyledDocument doc3 = artist.getStyledDocument();
            SimpleAttributeSet center3 = new SimpleAttributeSet();
            StyleConstants.setAlignment(center3, StyleConstants.ALIGN_LEFT);
            doc3.setParagraphAttributes(0, doc3.getLength(), center3, false);
            artist.setBounds(80, 69, 275, 25);
            this.add(artist);

            JTextPane inv = new JTextPane();
            inv.setText("");
            inv.setOpaque(false);
            inv.setBorder(new EmptyBorder(0, 0, 0, 0));
            inv.setBounds(10, 39, 60, 60);
            inv.setEditable(false);
            inv.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if (SpotifyPartyPanelChat.host) {
                        api.playTrack(info.getUri());
                    }
                }
            });
            this.add(inv);

            System.out.println(link + " " + str);
            Requests.backText.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 8));
            Requests.backText.setText(Requests.backText.getText() + "\n\n\n\n\n\n\n\n\n\n");

            animate(this);

    }

    public void animate(JComponent obj) {
        obj.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                api.playTrack(info.getUri());
            }

            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                song.setFont(new Font("CircularSpUIv3T-Bold", Font.BOLD, 18));
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                song.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 17));
            }
        });
    }

    public Track getData() {
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
            g2d.fillRoundRect(0, 30, 430, 80, 20, 20);
            //g.drawImage(ImageIO.read(getClass().getResource("/SpotifyBG.jpg")), 0, 0, 700, 600, this);
            g.drawImage(ImageIO.read(SpotifyUtils.getTrack(url).getAlbum().getImages().get(2).getUrl()), 10, 39, 60, 60, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return info.getUri() + ";" + name;
    }
}
