package chatGUI;
import exception.SpotifyException;
import gui.RequestTab;
import gui.Requests;
import gui.RoundJTextField;
import lyrics.LyricFinder;
import model.Artist;
import model.Item;
import model.UserData;
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
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static chatGUI.SpotifyPartyFrameChat.trayIcon;
import static chatGUI.SpotifyPartyPanelChat.spfc;
import static gui.Requests.icon;
import static main.SpotifyParty.defFont;
import static utils.GUIUtils.makeButton;
import static utils.GUIUtils.resizeIcon;
import static chatGUI.SpotifyPartyPanelChat.FriendName;
import static chatGUI.SpotifyPartyPanelChat.cli;
import static chatGUI.SpotifyPartyPanelChat.host;
import static main.SpotifyParty.api;

public class ChatPanel extends JPanel{
    public static Color color = new Color(30, 30, 30);
    public static JTextPane area;
    public static JTextPane song;
    public static JTextPane artist;
    public static RoundJTextField code;
    public static Chat chat = new Chat();
    public static RoundJTextField type = new RoundJTextField(380, 33);
    public JScrollPane areaScroll;
    private URL artworkURL;
    public final static String[] theCode = {""};
    public static JTextPane chatSwitch;
    public static JTextPane reqSwitch;
    public static boolean chatCheck = true;
    public static CardLayout cl = new CardLayout();
    public static Requests requestPanel = new Requests();
    private static int WIN = 0;
    public static HashMap names = new HashMap<>();
    public boolean privateSwitch = false;
    public ChatPanel() {
        if(System.getProperty("os.name").contains("Windows"))
        {
            WIN = -20;
        }
        putClientProperty("Aqua.backgroundStyle", "vibrantUltraDark");
        this.setLayout(null);
        JPanel back = new JPanel();
        back.putClientProperty("Aqua.backgroundStyle", "vibrantUltraDark");
        back.setLayout(cl);
        back.add(chat, "ChatPanel");
        back.add(requestPanel, "RequestPanel");
        cl.show(back, "ChatPanel");
        if(System.getProperty("os.name").contains("Windows"))
            setBackground(new Color(30, 30, 30));
        putClientProperty("Aqua.backgroundStyle", "vibrantUltraDark");
        putClientProperty("Aqua.windowStyle", "noTitleBar");
        code = new RoundJTextField(200, 30);
        code.setFont(new Font(defFont, Font.PLAIN, 8));

        try {
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(getClass().getResource("/fonts/Arial Unicode MS.ttf").getFile())));
            System.out.println(Arrays.toString(ge.getAllFonts()));
        } catch (IOException | FontFormatException e) {
           // e.printStackTrace();
        }
        code.setFocusable(false);
        code.setForeground(Color.GRAY);
        code.setBounds(47, 31+WIN, 195, 30);
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
        song.setFont(new Font(defFont, Font.PLAIN, 13));
        StyledDocument doc = song.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        song.setBounds(10, 547+WIN, 230, 17);
        this.add(song);

        artist = new JTextPane();
        artist.setFocusable(false);
        artist.setBorder(new EmptyBorder(0, 0, 0, 0));
        artist.setOpaque(false);
        artist.setForeground(Color.GRAY);
        artist.setEditable(false);
        artist.setFont(new Font(defFont, Font.PLAIN, 13));
        StyledDocument doc2 = artist.getStyledDocument();
        SimpleAttributeSet center2 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center2, StyleConstants.ALIGN_CENTER);
        doc2.setParagraphAttributes(0, doc2.getLength(), center2, false);
        artist.setBounds(10, 567+WIN, 230, 17);
        this.add(artist);


        area = new JTextPane();
        area.setText("Loading");
        area.setFocusable(false);
        area.setBorder(new EmptyBorder(0, 0, 0, 0));
        area.setAutoscrolls(true);
        area.setEditable(false);
        area.setForeground(Color.WHITE);
        area.setFont(new Font(defFont, Font.PLAIN, 15));
        area.setOpaque(false);
        StyledDocument doc3 = area.getStyledDocument();
        SimpleAttributeSet center3 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center3, StyleConstants.ALIGN_CENTER);
        doc3.setParagraphAttributes(0, doc3.getLength(), center3, false);
        area.setBackground(new Color(40, 40, 40));
        try {
            if(System.getProperty("os.name").contains("Mac"))
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
        areaScroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        areaScroll.setBounds(25, 120+WIN, 210, 250);
        areaScroll.getVerticalScrollBar().setUnitIncrement(4);
        this.add(areaScroll);
        addLyrics();
        try {
            if(System.getProperty("os.name").contains("Mac"))
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
        type.setFont(new Font(defFont,Font.PLAIN, 15));
        type.setForeground(Color.WHITE);
        type.setBounds(260, 552+WIN, 380, 35);
        type.setBackground(new Color(110, 110, 110));
        type.setCaretColor(Color.GREEN);
        this.add(type);
        ImageIcon playIcon = resizeIcon(new ImageIcon(getClass().getResource("/images/play.png")), 38, 38);
        AbstractButton play = makeButton(playIcon);
        play.setBounds(647, 550+WIN, 38, 38);

        play.addActionListener(e -> {
            if(!type.getText().isEmpty() && !type.getText().isBlank()) {
                if (chatCheck) {
                    chat.addText(type.getText(), SpotifyPartyPanelChat.FriendName);
                    if (!host)
                        cli.sendToServer("chat " + SpotifyPartyPanelChat.FriendName + " " + type.getText());
                    else
                        server.SketchServer.sendToClients("chat " + SpotifyPartyPanelChat.FriendName + " " + type.getText());
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
                if (chatCheck) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER && !type.getText().isEmpty() && !type.getText().isBlank()) {
                        try {
                            chat.addText(type.getText(), SpotifyPartyPanelChat.FriendName);
                            //trayIcon.displayMessage("hi", "f", TrayIcon.MessageType.NONE);
                        }catch (Exception e1)
                        {
                            e1.printStackTrace();
                        }
                        if (!host)
                            cli.sendToServer("chat " + SpotifyPartyPanelChat.FriendName.replace(" ", "-") + " " + type.getText());
                        else
                            server.SketchServer.sendToClients("chat " + SpotifyPartyPanelChat.FriendName.replace(" ", "-") + " " + type.getText());
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
        JTextPane name = new JTextPane();

        lyrics.setFont(new Font(defFont, Font.PLAIN, 21));
        lyrics.setForeground(Color.GREEN);
        lyrics.setText("Lyrics");
        lyrics.setBorder(new EmptyBorder(0, 0, 0, 0));
        lyrics.setOpaque(false);
        lyrics.setFocusable(false);
        lyrics.setEditable(false);
        lyrics.setBounds(150, 75+WIN, 70, 30);
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
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                lyrics.setForeground(Color.GREEN);
                name.setForeground(Color.WHITE);
                addLyrics();
            }
        });
        this.add(lyrics);

        name.setForeground(Color.WHITE);
        name.setFont(new Font(defFont, Font.PLAIN, 21));
        name.setForeground(Color.WHITE);
        name.setText("Friends");
        name.setBorder(new EmptyBorder(0, 0, 0, 0));
        name.setOpaque(false);
        name.setFocusable(false);
        name.setEditable(false);
        name.setBounds(40, 75+WIN, 75, 30);
        name.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                name.setFont(new Font("CircularSpUIv3T-Bold", Font.BOLD, 21));
                name.setText("Friends");
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                name.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 21));
                name.setText("Friends");
            }
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                name.setForeground(Color.GREEN);
                lyrics.setForeground(Color.WHITE);
                addNames();
            }
        });
        this.add(name);

        reqSwitch = new JTextPane();
        StyledDocument doc4 = reqSwitch.getStyledDocument();
        SimpleAttributeSet center4 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center4, StyleConstants.ALIGN_CENTER);
        doc4.setParagraphAttributes(0, doc4.getLength(), center4, false);
        reqSwitch.setForeground(Color.WHITE);
        reqSwitch.setEditable(false);
        reqSwitch.setOpaque(false);
        reqSwitch.setFocusable(false);
        reqSwitch.setBorder(new EmptyBorder(0, 0, 0, 0));
        reqSwitch.setText("Request");
        reqSwitch.setFont(new Font(defFont, Font.PLAIN, 30));
        reqSwitch.setBounds(430, 20+WIN, 255, 45);
        reqSwitch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                reqSwitch.setFont(new Font(defFont, Font.BOLD, 30));
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                reqSwitch.setFont(new Font(defFont, Font.PLAIN, 30));
            }
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                reqSwitch.setForeground(Color.GREEN);
                chatSwitch.setForeground(Color.WHITE);
                cl.show(back, "RequestPanel");
                chatCheck = false;
            }
        });
        this.add(reqSwitch);

        chatSwitch = new JTextPane();
        StyledDocument doc5 = chatSwitch.getStyledDocument();
        SimpleAttributeSet center5 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center5, StyleConstants.ALIGN_CENTER);
        doc5.setParagraphAttributes(0, doc4.getLength(), center5, false);
        chatSwitch.setForeground(Color.GREEN);
        chatSwitch.setEditable(false);
        chatSwitch.setOpaque(false);
        chatSwitch.setFocusable(false);
        chatSwitch.setBorder(new EmptyBorder(0, 0, 0, 0));
        chatSwitch.setText("Chat");
        chatSwitch.setFont(new Font(defFont, Font.PLAIN, 30));
        chatSwitch.setBounds(250, 20+WIN, 255, 45);
        chatSwitch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                chatSwitch.setFont(new Font(defFont, Font.BOLD, 30));
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                chatSwitch.setFont(new Font(defFont, Font.PLAIN, 30));
            }
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                chatSwitch.setForeground(Color.GREEN);
                reqSwitch.setForeground(Color.WHITE);
                cl.show(back, "ChatPanel");
                chatCheck = true;
            }
        });
        this.add(chatSwitch);

        /*
        JTextPane invs = new JTextPane();
        invs.setEditable(false);
        invs.setOpaque(false);
        invs.setFocusable(false);
        invs.setBorder(new EmptyBorder(0, 0, 0, 0));
        invs.setBounds(415, 35+WIN, 105, 28);
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
                req.setFont(new Font(defFont, Font.BOLD, 33));
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                req.setFont(new Font(defFont, Font.PLAIN, 30));
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
        guest.setBounds(10, 30+WIN, 24, 24);
        this.add(guest);
        */

        back.setBounds(250, 70+WIN, 450, 460);
        this.add(back);

        JTextPane priSwitch = new JTextPane();
        priSwitch.setOpaque(false);
        priSwitch.setEditable(false);
        priSwitch.setFocusable(false);
        priSwitch.setBounds(10, 31+WIN, 30, 30);
        priSwitch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(!host) {
                    privateSwitch = !privateSwitch;
                    repaint();
                    System.out.println("PRIVATE MODE SWITCH PRESSED");
                }
            }
        });
        add(priSwitch);
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
            } else if (type.getText().trim().toLowerCase().contains("replay!")) {
                api.previousTrack();
                type.setText("");
            } else if (type.getText().trim().toLowerCase().contains("egg!")) {
                String op = "abcdefghijklmnopqrstuvwxyz";
                int pos = (int)(Math.random() * (op.length() - 1));
                int pos2 = (int)(Math.random() * (op.length() - 1));
                String search = op.substring(pos, pos + 1) + op.substring(pos2, pos2 + 1);
                search = search.toString();
                Item item = api.search(search.toLowerCase(), 50).getTracks().getItems().get((int)(Math.random() * 50));
                try {
                    api.playTrack(item.getUri());
                    type.setText("");
                } catch (Exception e) {
                    api.playTrack("spotify:track:42C9YmmOF7PkiHWpulxzcq");
                    type.setText("");
                }
            } else {
                try {
                   Item temp = api.search(type.getText().toLowerCase(), 5).getTracks().getItems().get(0);
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
                Item item;
                try {
                    item = api.search(type.getText().toLowerCase(), 5).getTracks().getItems().get(0);
                    RequestTab tab = new RequestTab(item.getUri(), SpotifyPartyPanelChat.FriendName);
                    Requests.addRequest(tab);
                    cli.sendToServer("request " + item.getUri() + " " + FriendName);
                    type.setText("");
                } catch (Exception e) {
                    try {
                        item = api.search(type.getText(), 5).getTracks().getItems().get(0);
                        RequestTab tab = new RequestTab(item.getUri(), SpotifyPartyPanelChat.FriendName);
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

    /*
    private void recommendationHandler() {
        Item item = null;
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
                            api.playTrack(api.search(str).getTracks().getItems().get(0).getUri());
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

                } else {
                    work = true;
                    try {
                        //api.playTrack(SpotifyUtils.getTrackInfo(type.getText().trim()).getUri());
                    } catch (Exception e) {
                        String str = type.getText().trim().toLowerCase().replaceAll("[^a-zA-Z0-9]", " ");
                        try {

                            tab = new RequestTab(api.search(type.getText().trim()).getTracks().getItems().get(0).getUri(), SpotifyPartyPanelChat.FriendName);
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
                    api.playTrack(api.search(type.getText().toLowerCase().trim().replaceAll("[^a-zA-Z0-9]", " ")).getTracks().getItems().get(0).getUri());
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
                    tab = new RequestTab(api.search(type.getText().trim()).getTracks().getItems().get(0).getUri(), SpotifyPartyPanelChat.FriendName);
                    tab = new RequestTab(type.getText().trim(), SpotifyPartyPanelChat.FriendName);
                    if (SpotifyPartyPanelChat.host)
                        TCPServer.sendToClients("request " + tab.toString().split(";")[0].trim() + " " + SpotifyPartyPanelChat.FriendName);
                    else
                        cli.sendToServer("request " + tab.toString().split(";")[0] + " " + SpotifyPartyPanelChat.FriendName);
                } catch (Exception e1) {
                    try {
                        tab = new RequestTab(api.search(type.getText().toLowerCase()).getTracks().getItems().get(0).getUri(), SpotifyPartyPanelChat.FriendName);
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

     */

    public static void addNames() {
        area.setFont(new Font(defFont, Font.PLAIN, 18));
        StringBuilder format = new StringBuilder();
        for(Object names : names.keySet())
        {
            format.append(names).append("\n\n");
        }
        area.setText(format.toString());
    }

    public void addLyrics() {
        area.setFont(new Font(defFont, Font.PLAIN, 15));
        try {
            areaScroll.getVerticalScrollBar().setValue(0);
            area.setText(LyricFinder.getLyrics(song.getText(), artist.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Item updateData(String trackID) {
        System.out.println("before if");
        if(trackID != null) {
            System.out.println("after if");
            Item inf = api.getTrackInfo(trackID);
            System.out.println("after item");
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
        System.out.println("shit screwed up");
       return null;
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
    public Item updateData(){
        try {
            return updateData(api.getTrackUri());
        } catch (IOException | NullPointerException e) {
            return updateData( null);
        }
    }
    public static void setCode(String tcode) {
        code.setFont(new Font(defFont, Font.PLAIN, 11));
        code.setText(tcode);
    }
    public static UserData dat;
    private BufferedImage profile = null;
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


            if(profile == null)
            {
                UserData dat = api.getUserData();
                if(dat.getImages() == null || dat.getImages().isEmpty())
                {
                    profile = ImageIO.read(getClass().getResource("/images/logo.png"));
                }else
                {
                    profile = GUIUtils.circleCrop(ImageIO.read(dat.getImages().get(0).getUrl()));
                }
            }
            g.drawImage(profile, 10, 31+WIN, 30, 30, this);

            if(privateSwitch) {
                g2d.setColor(Color.RED);
                g2d.drawLine(10, 61+WIN, 40, 31+WIN);
            }

            if (artworkURL != null)
                g.drawImage(ImageIO.read(artworkURL), 50, 384+WIN, 150, 150, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}