package chatGUI;
import interfaces.SpotifyPlayerAPI;
import model.TrackInfo;
import spotifyAPI.SpotifyAppleScriptWrapper;
import utils.SpotifyUtils;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import static chatGUI.GUIUtilsChat.makeButton;
import static chatGUI.GUIUtilsChat.resizeIcon;

public class ChatPanel extends JPanel {
    public static SpotifyPlayerAPI api = new SpotifyAppleScriptWrapper();
    public static Color color = new Color(40,40,40);
    public static JTextPane area;
    public static JTextPane song;
    public static JTextPane artist;
    public static RoundJTextField code;
    public static AbstractButton copy;
    public static Chat chat  = new Chat();
    public static RoundJTextField type;
    public static ArrayList<String> nameList = new ArrayList<>();
    public static HashSet<String> names = new HashSet<>();
    public static int rangeL = 0;
    public static int rangeU = 3;
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
        text.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 40));
        text.setForeground(Color.WHITE);
        text.setBounds(-70, 50, 400, 100);
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

        area = new JTextPane();
        area.setForeground(Color.WHITE);
        area.setBorder(new EmptyBorder(0,0,0,0));
        area.setAutoscrolls(true);
        area.setOpaque(false);
        area.setFocusable(false);
        area.setEditable(false);
        area.setBounds(0, 0, 200, 220);
        area.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 20));
        area.setText("hello");
        StyledDocument doc3 = area.getStyledDocument();
        SimpleAttributeSet center3 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center3, StyleConstants.ALIGN_CENTER);
        doc3.setParagraphAttributes(0, doc3.getLength(), center3, false);
        JScrollPane areaScroll = new JScrollPane(area);
        areaScroll.add(area);
        areaScroll.setOpaque(false);
        areaScroll.getViewport().setOpaque(false);
        areaScroll.setAutoscrolls(true);
        areaScroll.setBorder(new EmptyBorder(0,0,0,0));
        areaScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 300));
        areaScroll.setSize(200, 250);
        areaScroll.setBounds(25, 145, 200, 220);
        areaScroll.addMouseWheelListener(e -> {
            if(e.getWheelRotation() >= 2)
                scrollUp();
            else if(e.getWheelRotation() <= -2)
                scrollDown();
        });
                addNames("whats popin", "yo", "dababay", "yam", "holy fuck", "fuck a do", "dhanush", "emilia");
        //this.add(area);
        this.add(areaScroll);

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
                chat.addRequest(tab);
                type.setText("");
            } catch (Exception e1) {
                type.setText("INVALID URI");
            }
        });
        this.add(play);

        JViewport scroll = new JViewport();
        scroll.setSize(450, 520);
        scroll.setBounds(250, 0, 450, 520);
        scroll.setOpaque(false);
        scroll.setView(chat);
        scroll.setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
        scroll.setAutoscrolls(true);
        this.add(scroll);
    }
    public void scrollUp()
    {
        StringBuffer str = new StringBuffer();
        for(int i = --rangeU; i >= ++rangeL; i--) {
            str.append(nameList.get(i)).append("\n\n");
        }
        area.setText(str.toString());
    }
    public void scrollDown()
    {
        StringBuffer str = new StringBuffer();
        for(int i = ++rangeU; i >= --rangeL; i--) {
            str.append(nameList.get(i)).append("\n\n");
        }
        area.setText(str.toString());
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
       for(String nam:name)
       {
           if(!nam.isEmpty() && !nam.isBlank() && !names.contains(nam))

                   nameList.add(nam);
                   names.add(nam);

       }
        StringBuilder str = new StringBuilder();
        for(int i = rangeU; i >= rangeL; i--) {
                str.append(nameList.get(i)).append("\n\n");
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
        code.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 11));
        code.setText(" " + tcode);
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
                g.drawImage(ImageIO.read(artworkURL), 50, 380, 155, 155, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
