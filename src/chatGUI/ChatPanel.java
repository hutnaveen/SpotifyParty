package chatGUI;

import client.TCPClient;
import exception.SpotifyException;
import interfaces.SpotifyPlayerAPI;
import main.SpotifyParty;
import model.TrackInfo;
import server.TCPServer;
import spotifyAPI.SpotifyAppleScriptWrapper;
import utils.SpotifyUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static chatGUI.SpotifyPartyPanelChat.cli;
import static gui.GUIUtil.makeButton;
import static gui.GUIUtil.resizeIcon;

public class ChatPanel extends JPanel {
    public static SpotifyPlayerAPI api = new SpotifyAppleScriptWrapper();
    public static Color color = new Color(30,30,30);
    public static JTextPane area;
    public static JTextPane song;
    public static JTextPane artist;
    public static RoundJTextField code;
    public static AbstractButton copy;
    public static Chat chat  = new Chat();
    public static RoundJTextField type;
    public static HashSet<String> names = new HashSet<>();
    public static String uri = "";
    private URL artworkURL;

    public boolean me = true;


    public ChatPanel() {
        this.setLayout(null);
        putClientProperty("Aqua.backgroundStyle", "vibrantUltraDark");
        putClientProperty("Aqua.windowStyle", "noTitleBar");
        code = new RoundJTextField(200);
        code.setBorder(new EmptyBorder(0,0,0,0));
        code.setForeground(Color.GRAY);
        code.setBounds(40, 10, 195, 30);
        code.setEditable(false);
        code.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                StringSelection selection = new StringSelection(code.getText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            }
        });
        this.add(code);

        JLabel text = new JLabel("Friends", SwingConstants.CENTER);
        text.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 30));
        text.setForeground(Color.WHITE);
        text.setBounds(-70, 20, 400, 100);
        this.add(text);

        song = new JTextPane();
        song.setBorder(new EmptyBorder(0, 0, 0 ,0));
        song.setOpaque(false);
        song.setForeground(Color.WHITE);
        song.setEditable(false);
        song.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 13));
        StyledDocument doc = song.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        song.setBounds(10, 527, 230, 17);
        this.add(song);

        artist = new JTextPane();
        artist.setBorder(new EmptyBorder(0, 0, 0 ,0));
        artist.setOpaque(false);
        artist.setForeground(Color.GRAY);
        artist.setEditable(false);
        artist.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 13));
        StyledDocument doc2 = artist.getStyledDocument();
        SimpleAttributeSet center2 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center2, StyleConstants.ALIGN_CENTER);
        doc2.setParagraphAttributes(0, doc2.getLength(), center2, false);
        artist.setBounds(10, 547, 230, 17);
        this.add(artist);


        area = new JTextPane();
        area.setBorder(new EmptyBorder(0, 0, 0 ,0));
        area.setAutoscrolls(true);
        area.setEditable(false);
        area.setForeground(Color.WHITE);
        //addNames("fuck","shaush", "emilia", "is", "hotter", "than ", "emma", "watosn");
        area.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 15));
        area.setOpaque(false);
        StyledDocument doc3 = area.getStyledDocument();
        SimpleAttributeSet center3 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center3, StyleConstants.ALIGN_CENTER);
        doc3.setParagraphAttributes(0, doc3.getLength(), center3, false);
        area.setBackground(new Color(40, 40, 40));
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JScrollPane areaScroll = new JScrollPane();
        areaScroll.getViewport().setFocusable(false);
        areaScroll.getViewport().setView(area);
        areaScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        areaScroll.setOpaque(false);
        areaScroll.getViewport().setOpaque(false);
        areaScroll.setBackground(new Color(40, 40, 40));
        areaScroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 300));
        areaScroll.getVerticalScrollBar().setOpaque(false);
        areaScroll.getVerticalScrollBar().setBorder(new EmptyBorder(0,0,0,0));
        areaScroll.getVerticalScrollBar().setBackground(new Color(30, 30, 30));
        areaScroll.setBounds(25, 110, 200, 250);
        this.add(areaScroll);
        try {
            UIManager.setLookAndFeel("org.violetlib.aqua.AquaLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        RoundJTextField type = new RoundJTextField(380);
        type.setBounds(260, 525, 380, 40);
        type.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                type.setText("");
            }
            public void mouseExited(MouseEvent e) {
                try {
                    uri = type.getText();
                    type.setText(SpotifyUtils.getTrackInfo(type.getText()).getName() + " - " + SpotifyUtils.getTrackInfo(type.getText()).getArtist());
                } catch(Exception e1) {
                    uri = type.getText();
                }
            }
        });
        this.add(type);
        ImageIcon playIcon = resizeIcon(new ImageIcon(getClass().getResource("/Untitled.png")), 40, 40);
        AbstractButton play = makeButton("", playIcon);
        play.setBounds(645, 525, 40, 40);
        play.addActionListener(e -> {
            try {
                RequestTab tab = new RequestTab(uri, SpotifyPartyPanelChat.FriendName);
                chat.addRequest(tab);
                if(SpotifyPartyPanelChat.host)
                    TCPServer.sendToClients("request " + tab.toString().split(";")[0].trim()+ " " +SpotifyPartyPanelChat.FriendName, null);
                else
                    cli.writeToServer("request " + tab.toString().split(";")[0] + " " + SpotifyPartyPanelChat.FriendName);
                type.setText("");
            } catch (Exception e1) {
                e1.printStackTrace();
                type.setText("INVALID URI");
            }
        });
        this.add(play);

        JScrollPane chatScroll = new JScrollPane();
        chatScroll.getViewport().setView(Chat.back);
        chatScroll.setBounds(250, 0, 450, 517);
        chatScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        chatScroll.setOpaque(false);
        chatScroll.getViewport().setOpaque(false);
        chatScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 517));
        chatScroll.getVerticalScrollBar().setOpaque(false);
        chatScroll.getVerticalScrollBar().setBorder(new EmptyBorder(0,0,0,0));
        chatScroll.getVerticalScrollBar().setUnitIncrement(16);
        chatScroll.getVerticalScrollBar().setBackground(new Color(30, 30, 30));

        this.add(chatScroll);

    }

    public static void addNames(String... name) {
        for(String num: name) {
            if (!num.isBlank() && !num.isEmpty()) {
                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(num);
                if(!m.find())
                    names.add(num);
            }
        }
        String str = "";
        for(String num:  names) {
            str = str + (" " +num.trim() + "\n\n");
        }
        area.setText(str);
    }

    public TrackInfo updateData(String trackID)
    {
        TrackInfo inf = SpotifyUtils.getTrackInfo(trackID);
        artworkURL = inf.getThumbnailURL();
        song.setText(inf.getName());
        artist.setText(inf.getArtist());
        color = inf.getDominantColor().darker();
        repaint();
        return inf;
    }

    public TrackInfo updateData()
    {
        return updateData(api.getTrackId());
    }
    public static void setCode(String tcode)
    {
        code.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 11));
        code.setText(tcode);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            Graphics2D g2d = (Graphics2D) g;
            Color color1 = color.brighter();
            Color color2 = color.darker().darker().darker();
            GradientPaint gp = new GradientPaint(
                    0, 0, color1, 0, 600, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, 250, 600);

            g.drawImage(ImageIO.read(getClass().getResource("/logo.png")), 10, 14, 24, 24, this);
            if(artworkURL != null)
                g.drawImage(ImageIO.read(artworkURL), 55, 380, 140, 140, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}