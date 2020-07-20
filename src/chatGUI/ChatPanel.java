package chatGUI;
import exception.SpotifyException;
import gui.RequestTab;
import gui.Requests;
import gui.RoundJTextField;
import interfaces.SpotifyPlayerAPI;
import lyrics.LyricFinder;
import model.Artist;
import model.Track;
import server.TCPServer;
import spotifyAPI.SpotifyAppleScriptWrapper;
import utils.GUIUtils;
import utils.SpotifyUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;

import static utils.GUIUtils.makeButton;
import static utils.GUIUtils.resizeIcon;
import static chatGUI.SpotifyPartyPanelChat.FriendName;
import static chatGUI.SpotifyPartyPanelChat.cli;
import static chatGUI.SpotifyPartyPanelChat.host;


public class ChatPanel extends JPanel{
    public static SpotifyPlayerAPI api = new SpotifyAppleScriptWrapper();
    public static Color color = new Color(30, 30, 30);
    public static JTextPane area;
    public static JTextPane song;
    public static JTextPane artist;
    public static RoundJTextField code;
    public static JTextField guest = new JTextField();
    public static Chat chat = new Chat();
    public static HashSet<String> names = new HashSet<>();
    public static RoundJTextField type = new RoundJTextField(380);
    public JScrollPane areaScroll;
    private URL artworkURL;
    public final static String[] theCode = {""};
    public boolean chatSwitch = true;
    public static JTextPane req;
    public static CardLayout cl = new CardLayout();
    public static Requests requestPanel = new Requests();
    public static AbstractButton mode;
    public static ImageIcon enabled = resizeIcon(new ImageIcon(ChatPanel.class.getResource("/images/logo.png")), 24, 24);
    public static ImageIcon disabled = resizeIcon(new ImageIcon(ChatPanel.class.getResource("/images/neglogo.png")), 24, 24);
    static {
        ImageIcon ic = enabled;
        mode = makeButton(ic);
    }
    public ChatPanel() {
        putClientProperty("Aqua.backgroundStyle", "vibrantUltraDark");
        this.setLayout(null);
        JPanel back = new JPanel();
        back.putClientProperty("Aqua.backgroundStyle", "vibrantUltraDark");
        back.setLayout(cl);
        back.add(chat, "ChatPanel");
        back.add(requestPanel, "RequestPanel");
        mode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!host)
                {
                    cli.synced = !cli.synced;
                    if(cli.synced)
                    {
                        mode.setIcon(enabled);
                    }
                    else
                    {
                        mode.setIcon(disabled);
                    }
                }
            }
        });
        cl.show(back, "ChatPanel");

        putClientProperty("Aqua.backgroundStyle", "vibrantUltraDark");
        putClientProperty("Aqua.windowStyle", "noTitleBar");
        code = new RoundJTextField(200);
        code.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 8));
        try {
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(getClass().getResource("/fonts/Arial Unicode MS.ttf").getFile())));
            System.out.println(Arrays.toString(ge.getAllFonts()));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        code.setFocusable(false);
        code.setBorder(new EmptyBorder(0, 0, 0, 0));
        code.setForeground(Color.GRAY);
        code.setBounds(40, 30, 195, 30);
        code.setEditable(false);
        code.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                theCode[0] = code.getText();
                StringSelection selection = new StringSelection(code.getText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
                code.setText("Code Copied");
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                code.setText(theCode[0]);
            }
        });
        this.add(code);

        song = new JTextPane();
        song.setFocusable(false);
        song.setBorder(new EmptyBorder(0, 0, 0, 0));
        song.setOpaque(false);
        song.setForeground(Color.WHITE);
        song.setEditable(false);
        song.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 13));
        StyledDocument doc = song.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        song.setBounds(10, 547, 230, 17);
        this.add(song);

        artist = new JTextPane();
        artist.setFocusable(false);
        artist.setBorder(new EmptyBorder(0, 0, 0, 0));
        artist.setOpaque(false);
        artist.setForeground(Color.GRAY);
        artist.setEditable(false);
        artist.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 13));
        StyledDocument doc2 = artist.getStyledDocument();
        SimpleAttributeSet center2 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center2, StyleConstants.ALIGN_CENTER);
        doc2.setParagraphAttributes(0, doc2.getLength(), center2, false);
        artist.setBounds(10, 567, 230, 17);
        this.add(artist);


        area = new JTextPane();
        area.setText("Loading");
        area.setFocusable(false);
        area.setBorder(new EmptyBorder(0, 0, 0, 0));
        area.setAutoscrolls(true);
        area.setEditable(false);
        area.setForeground(Color.WHITE);
        area.setFont(new Font("Arial Unicode MS", Font.PLAIN, 15));
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
        areaScroll = new JScrollPane();
        areaScroll.getViewport().setFocusable(false);
        areaScroll.getViewport().setView(area);
        areaScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        areaScroll.setOpaque(false);
        areaScroll.getViewport().setOpaque(false);
        areaScroll.setBackground(new Color(40, 40, 40));
        areaScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 300));
        areaScroll.getVerticalScrollBar().setOpaque(false);
        areaScroll.getVerticalScrollBar().setBorder(new EmptyBorder(0, 0, 0, 0));
        areaScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        areaScroll.setBounds(25, 120, 210, 250);
        areaScroll.getVerticalScrollBar().setUnitIncrement(4);
        this.add(areaScroll);
        addLyrics();
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
        type.setFont(new Font("CircularSpUIv3T-Light",Font.BOLD, 15));
        type.setBounds(260, 545, 380, 40);
        type.setCaretColor(Color.GREEN);
        this.add(type);
        ImageIcon playIcon = resizeIcon(new ImageIcon(getClass().getResource("/images/play.png")), 40, 40);
        AbstractButton play = makeButton(playIcon);
        play.setBounds(647, 545, 40, 40);

        play.addActionListener(e -> {
            if(!type.getText().isEmpty() && !type.getText().isBlank()) {
                if (chatSwitch) {
                    chat.addText(type.getText(), SpotifyPartyPanelChat.FriendName);
                    if (!host)
                        cli.sendToServer("chat " + SpotifyPartyPanelChat.FriendName + " " + type.getText());
                    else
                        server.TCPServer.sendToClients("chat " + SpotifyPartyPanelChat.FriendName + " " + type.getText());
                    type.setText("");
                } else {
                    //recommendationHandler();
                    recHandler();
                }
            }
        });
        type.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (chatSwitch) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER && !type.getText().isEmpty() && !type.getText().isBlank()) {
                        chat.addText(type.getText(), SpotifyPartyPanelChat.FriendName);
                        if (!host)
                            cli.sendToServer("chat " + SpotifyPartyPanelChat.FriendName + " " + type.getText());
                        else
                            server.TCPServer.sendToClients("chat " + SpotifyPartyPanelChat.FriendName + " " + type.getText());
                        type.setText("");
                    }
                } else {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER && !type.getText().isEmpty() && !type.getText().isBlank()) {
                        //recommendationHandler();
                        recHandler();
                    }
                }
            }
        });
        this.add(play);

        JTextPane lyrics = new JTextPane();
        lyrics.setForeground(Color.WHITE);
        lyrics.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 21));
        lyrics.setForeground(Color.WHITE);
        lyrics.setText("Lyrics");
        lyrics.setBorder(new EmptyBorder(0, 0, 0, 0));
        lyrics.setOpaque(false);
        lyrics.setFocusable(false);
        lyrics.setEditable(false);
        lyrics.setBounds(101, 75, 70, 30);
        /*
        lyrics.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                lyrics.setFont(new Font("CircularSpUIv3T-Bold", Font.BOLD, 21));
                lyrics.setText("Lyrics");
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                lyrics.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 21));
                lyrics.setText("Lyrics");
            }
        });
         */
        this.add(lyrics);

        req = new JTextPane();
        StyledDocument doc4 = req.getStyledDocument();
        SimpleAttributeSet center4 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center4, StyleConstants.ALIGN_CENTER);
        doc4.setParagraphAttributes(0, doc4.getLength(), center4, false);
        req.setForeground(Color.WHITE);
        req.setEditable(false);
        req.setOpaque(false);
        req.setFocusable(false);
        req.setBorder(new EmptyBorder(0, 0, 0, 0));
        req.setText("Party Chat");
        req.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 30));
        req.setBounds(340, 20, 255, 45);

        JTextPane invs = new JTextPane();
        invs.setEditable(false);
        invs.setOpaque(false);
        invs.setFocusable(false);
        invs.setBorder(new EmptyBorder(0, 0, 0, 0));
        invs.setBounds(415, 35, 105, 28);
        invs.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(chatSwitch) {
                    req.setText("Song Requests");
                    cl.show(back, "RequestPanel");
                } else {
                    req.setText("Party Chat");
                    cl.show(back, "ChatPanel");
                }
                chatSwitch = !chatSwitch;
            }
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                req.setFont(new Font("CircularSpUIv3T-Bold", Font.BOLD, 30));
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                req.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 30));
            }
        });
        this.add(invs);
        this.add(req);
        /*
        guest.setBorder(new EmptyBorder(0, 0, 0, 0));
        guest.setEditable(false);
        guest.setOpaque(false);
        guest.setFocusable(false);
        guest.setFont(new Font("CircularSpUIv3T-Bold", Font.BOLD, 14));
        guest.setForeground(Color.WHITE);
        guest.setText("0");
        guest.setBounds(10, 30, 24, 24);
        this.add(guest);
        */

        if(!host) {
            this.add(mode);
            mode.setBounds(10, 33, 24, 24);
        }else
        {
            mode.setEnabled(false);
        }
        mode.setFocusable(false);
        back.setBounds(250, 70, 450, 460);
        this.add(back);
    }


    public void recHandler() {
        if(host) {
            if (type.getText().trim().toLowerCase().equals("pause!")) {
                api.pause();
                type.setText("");
            } else if (type.getText().trim().toLowerCase().equals("play!")) {
                api.play();
                type.setText("");
            } else if (type.getText().trim().toLowerCase().contains("next!")) {
                api.nextTrack();
                type.setText("");
            } else if (type.getText().trim().toLowerCase().contains("prev!")) {
                api.previousTrack();
                type.setText("");
            } else {
                try {
                   Track temp = SpotifyUtils.findSong(type.getText().toLowerCase());
                    if(temp != null ) {
                        api.playTrack(temp.getUri());
                        type.setText("");
                    } else {
                        throw new SpotifyException("");
                    }
                } catch (Exception e) {
                    try {
                        if(api.playTrack(type.getText()) == false) {
                            throw new SpotifyException("");
                        }
                        type.setText("");
                    } catch (Exception e1) {
                        api.playTrack("spotify:track:42C9YmmOF7PkiHWpulxzcq");
                        type.setText("Cant find song give this a listen instead ;)");
                    }
                }
            }
        } else {
            if (type.getText().trim().toLowerCase().equals("pause!")) {
                api.pause();
            } else if (type.getText().trim().toLowerCase().equals("play!")) {
                api.play();
            } else {
                Track track;
                try {
                    track = SpotifyUtils.findSong(type.getText().trim());
                    RequestTab tab = new RequestTab(track.getUri(), SpotifyPartyPanelChat.FriendName);
                    Requests.addRequest(tab);
                    cli.sendToServer("request " + track.getUri() + " " + FriendName);
                    type.setText("");
                } catch (Exception e) {
                    try {
                        track = SpotifyUtils.getTrack(type.getText());
                        RequestTab tab = new RequestTab(track.getUri(), SpotifyPartyPanelChat.FriendName);
                        Requests.addRequest(tab);
                        cli.sendToServer("request " + type.getText() + " " + FriendName);
                        type.setText("");
                    } catch (Exception e1) {
                        type.setText("Cannot find song");
                    }
                }
            }
        }
    }


    private void recommendationHandler() {
        Track track = null;
        boolean work = true;
        RequestTab tab = null;
        if (!type.getText().isBlank() && !type.getText().isEmpty()) {
            if (type.getText().trim().toLowerCase().equals("pause!")) {
                api.pause();
            } else if (type.getText().trim().toLowerCase().equals("play!")) {
                api.play();
            } else if (type.getText().trim().toLowerCase().equals("stop!")) {
                System.exit(0);
            } else if (host) {
                if (type.getText().trim().toLowerCase().contains("next!")) {
                    api.nextTrack();
                } else if (type.getText().trim().toLowerCase().contains("prev!")) {
                    api.previousTrack();
                }
                    else if(type.getText().trim().toLowerCase().contains("play!:"))
                    {
                        try {
                            String str = type.getText().trim().toLowerCase();
                            str = str.substring(str.indexOf("play!:") + 6).trim();
                            api.playTrack(SpotifyUtils.findSong(type.getText().toLowerCase().trim().replaceAll("[^a-zA-Z0-9]", " ")).getUri());
                        }catch (Exception e)
                        {
                            work = false;
                        }
                        if(work)
                            type.setText("");
                        else {
                            type.setText("can't find that song");
                            type.selectAll();
                        }
                    }
                else if (type.getText().trim().toLowerCase().contains("history!")) {
                    /*
                    for (Track item : SpotifyPlayerHistory.getHistory()) {
                        work = true;
                        try {
                            api.playTrack(SpotifyUtils.getTrack(type.getText().trim()).getUri());
                        } catch (Exception e) {
                            String str = type.getText().trim().toLowerCase().replaceAll("[^a-zA-Z0-9]", " ");
                            try {
                                api.playTrack(SpotifyUtils.search(str).get(0));
                            } catch (Exception e1) {
                                work = false;
                            }
                        }
                        if (work)
                            type.setText("");
                        else {
                            type.setText("can't find that song");
                            type.selectAll();
                        }
                        if (item != null)
                            this.chat.addRequest(new RequestTab(item.getUri(), SpotifyPartyPanelChat.FriendName));
                    }

                     */
                } else {
                    work = true;
                    try {
                        //api.playTrack(SpotifyUtils.getTrackInfo(type.getText().trim()).getUri());
                    } catch (Exception e) {
                        String str = type.getText().trim().toLowerCase().replaceAll("[^a-zA-Z0-9]", " ");
                        try {

                            tab = new RequestTab(SpotifyUtils.search(type.getText().trim()).get(0).getUri(), SpotifyPartyPanelChat.FriendName);
                        } catch (Exception e3) {
                            type.setText("can't find that song");
                            type.selectAll();
                            work = false;
                            //api.playTrack(SpotifyUtils.findSong(str).getUri());
                        }
                    }
                    if (work)
                        type.setText("");
                    else {
                        type.setText("can't find that song");
                        type.selectAll();
                    }
                }
            } else {
                work = true;
                try {

                    System.out.println(type.getText());
                    api.playTrack(SpotifyUtils.search(type.getText().toLowerCase().trim().replaceAll("[^a-zA-Z0-9]", " ")).get(0).getUri());
                } catch (Exception e) {
                    System.out.println("Cannot find song, Sorry!");
                    type.setText("Cannot find song, Sorry!");
                }
            }
        } else {
            if (type.getText().equalsIgnoreCase("pause!")) {
                api.pause();
                type.setText("");
            } else if (type.getText().equalsIgnoreCase("play!")) {
                api.play();
                type.setText("");
            } else {
                try {
                    tab = new RequestTab(SpotifyUtils.search(type.getText().trim()).get(0).getUri(), SpotifyPartyPanelChat.FriendName);
                    tab = new RequestTab(type.getText().trim(), SpotifyPartyPanelChat.FriendName);
                    if (SpotifyPartyPanelChat.host)
                        TCPServer.sendToClients("request " + tab.toString().split(";")[0].trim() + " " + SpotifyPartyPanelChat.FriendName);
                    else
                        cli.sendToServer("request " + tab.toString().split(";")[0] + " " + SpotifyPartyPanelChat.FriendName);
                } catch (Exception e1) {
                    try {
                        tab = new RequestTab(SpotifyUtils.findSong(type.getText().trim()).getUri(), SpotifyPartyPanelChat.FriendName);
                    } catch (Exception e3) {
                        type.setText("can't find that song");
                        type.selectAll();
                        work = false;
                    }
                }
            }
            if (work) {
                if (tab != null)
                    Requests.addRequest(tab);
                type.setText("");
            }
        }
    }


    public void addLyrics() {
        try {
            areaScroll.getVerticalScrollBar().setValue(0);
            area.setText(LyricFinder.getLyrics(song.getText(), artist.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Track updateData(String trackID) {
        Track inf = SpotifyUtils.getTrack(trackID);
        artworkURL = inf.getAlbum().getImages().get(1).getUrl();
        song.setText(resize(inf.getName(), song.getFont(), 174));
        StringBuilder artists = new StringBuilder();
        for (Artist art : inf.getArtists()) {
            artists.append(art.getName() + ", ");
        }
        artists.replace(artists.length() - 2, artists.length(), "");
      //  System.out.println(GUIUtils.getTextWidth("Murda (feat. Cory Gunz, Cap", artist.getFont()));
        artist.setText(resize(artists.toString(), artist.getFont(), 194));
        color = inf.getDominantColor().darker();
        addLyrics();
        repaint();
        return inf;
    }
    private String resize(String str, Font font, int max)
    {
        if(GUIUtils.getTextWidth(str, font) <= max)
            return str;
        StringBuilder ret = new StringBuilder(str);
        while (GUIUtils.getTextWidth(ret.toString(), font) > max)
            ret.replace(ret.length()-1,ret.length(), "");
        return ret.toString()+ " ...";
    }
    public Track updateData() {
        return updateData(api.getTrackUri());
    }
    public static void setCode(String tcode) {
        code.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 11));
        code.setText(tcode);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            Graphics2D g2d = (Graphics2D) g;
            Color color1 = color.brighter();
            Color color2 = color.darker().darker().darker();
            GradientPaint gp = new GradientPaint(
                    0, 0, color1, 0, 600, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, 250, 600);
            if(host)
                g.drawImage(ImageIO.read(getClass().getResource("/images/logo.png")), 10, 33, 24, 24, this);
            if (artworkURL != null)
                g.drawImage(ImageIO.read(artworkURL), 55, 400, 140, 140, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}