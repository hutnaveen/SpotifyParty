package chatGUI;

import exception.SpotifyException;
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
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Arrays;

import static gui.GUIUtil.makeButton;
import static gui.GUIUtil.resizeIcon;

public class ChatPanel extends JPanel {
    public static SpotifyPlayerAPI api = new SpotifyAppleScriptWrapper();
    public static Color color = new Color(40,40,40);
    public static JTextPane area;
    public static JTextPane song;
    public static JTextPane artist;
    public static RoundJTextField code;
    public static HashSet<String> names = new HashSet<>();
    public static AbstractButton copy;
    public static Chat chat  = new Chat();
    public static RoundJTextField type;
    private URL artworkURL;

    @Override
    public void setBackground(Color bg) {
        //super.setBackground(bg);
    }

    public ChatPanel() {
        this.setLayout(null);
        putClientProperty("Aqua.backgroundStyle", "vibrantDark");
        area = new JTextPane();
        code = new RoundJTextField(200);
        code.setForeground(Color.GRAY);
        code.setBounds(40, 27, 195, 30);
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
        text.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 30));
        text.setForeground(Color.WHITE);
        text.setBounds(-70, 40, 400, 100);
        this.add(text);

        song = new JTextPane();
        song.setBorder(new EmptyBorder(0,0,0,0));
        song.setBackground(new Color(40, 40, 40));
        song.setForeground(Color.WHITE);
        song.setEditable(false);
        song.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 13));
        StyledDocument doc = song.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        song.setBounds(10, 553, 230, 17);
        this.add(song);

        artist = new JTextPane();
        artist.setBorder(new EmptyBorder(0,0,0,0));
        artist.setBackground(new Color(40, 40, 40));
        artist.setForeground(Color.GRAY);
        artist.setEditable(false);
        artist.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 13));
        StyledDocument doc2 = artist.getStyledDocument();
        SimpleAttributeSet center2 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center2, StyleConstants.ALIGN_CENTER);
        doc2.setParagraphAttributes(0, doc2.getLength(), center2, false);
        artist.setBounds(10, 573, 230, 17);
        this.add(artist);

        area.setBorder(new EmptyBorder(0,0,0,0));
        area.setAutoscrolls(true);
        area.setEditable(false);
        area.setBounds(25, 120, 200, 250);
        area.setForeground(Color.WHITE);
        area.setText("Hello");
        area.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 15));
        StyledDocument doc3 = area.getStyledDocument();
        SimpleAttributeSet center3 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center3, StyleConstants.ALIGN_CENTER);
        doc3.setParagraphAttributes(0, doc3.getLength(), center3, false);
        JScrollPane areaScroll = new JScrollPane();
        JViewport port = new JViewport();
        port.setView(area);
        port.setOpaque(false);
        port.setBounds(25, 130, 200, 250);
        areaScroll.setViewport(port);
        areaScroll.setBorder(new EmptyBorder(0,0,0,0));
        areaScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 300));
        //area.setBounds(25, 110, 200, 250);
        this.add(port);

        RoundJTextField type = new RoundJTextField(380);
        type.setBounds(260, 550, 380, 40);
        type.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                type.setText("");
            }
        });
        this.add(type);

        ImageIcon playIcon = resizeIcon(new ImageIcon(getClass().getResource("/Untitled.png")), 40, 40);
        AbstractButton play = makeButton("", playIcon);
        play.setBounds(650, 550, 40, 40);
        play.addActionListener(e -> {
            try {
                RequestTab tab = new RequestTab(type.getText());
                ChatPanel.chat.addRequest(tab);
                type.setText("");
            } catch (Exception e1) {
                type.setText("INVALID URI");
            }
        });
        this.add(play);

        this.add(chat);
    }

    public void setColor(Color c) {
        System.out.println(c);
        /*setBackground(c);
        song.setBackground(c);
        area.setBackground(c);
        artist.setBackground(c);
        areaScroll.setBackground(c);
        color = c;
        border = BorderFactory.createLineBorder(color, 1);
        song.setBorder(border);
        area.setBorder(border);
        artist.setBorder(border);
        areaScroll.setBorder(border);
        repaint();*/
    }

    public static void addNames(String... name) {
        names.addAll(Arrays.asList(name));
        StringBuilder str = new StringBuilder();
        for(String num: names) {
            if(!num.isBlank() && !num.isEmpty())
                str.append(" ").append(num).append("\n\n");
        }
        area.setText(str.toString());
    }

    public TrackInfo updateData(String trackID)
    {
        TrackInfo inf = SpotifyUtils.getTrackInfo(trackID);
        artworkURL = inf.getThumbnailURL();
        song.setText(inf.getName());
        artist.setText(inf.getArtist());
        setColor(inf.getDominantColor());
        repaint();
        return inf;
    }

    public TrackInfo updateData()
    {
        return updateData(api.getTrackId());
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
            g.drawImage(ImageIO.read(getClass().getResource("/SpotifyBG.jpg")), 250, 0, 550, 600, this);
           // g.setColor(this.getBackground());
            //g.fillRect(0, -100, 250, 700);
            g.drawImage(ImageIO.read(getClass().getResource("/logo.png")), 10, 32, 22, 22, this);
            //g.setColor(color.darker().darker().darker().darker().darker().darker().darker().darker().darker().darker().darker().darker().darker());
            if(artworkURL != null)
                g.drawImage(ImageIO.read(artworkURL), 70, 390, 115, 115, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
