package gui;
import history.SpotifyPlayerHistory;
import interfaces.SpotifyPlayerAPI;
import lyrics.LyricFinder;
import model.Artist;
import model.Track;
import server.TCPServer;
import spotifyAPI.SpotifyAppleScriptWrapper;
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
import java.net.URL;
import java.util.HashSet;

import static gui.GUIUtilsChat.makeButton;
import static gui.GUIUtilsChat.resizeIcon;
import static gui.SpotifyPartyPanelChat.cli;
import static gui.SpotifyPartyPanelChat.host;


public class ChatPanel extends JPanel implements DragGestureListener, DragSourceListener {
    public static SpotifyPlayerAPI api = new SpotifyAppleScriptWrapper();
    public static Color color = new Color(30, 30, 30);
    public static JTextPane area;
    public static JTextPane song;
    public static JTextPane artist;
    public static RoundJTextField code;
    public static JTextField guest = new JTextField();
    public static AbstractButton copy;
    public static Chat chat = new Chat();
    public static JScrollPane reqScroll;
    public static JViewport chatViewPort;
    public static HashSet<String> names = new HashSet<>();
    private static RoundJTextField type = new RoundJTextField(380);
    public JScrollPane areaScroll;
    private URL artworkURL;
    final String[] theCode = {""};
    public boolean chatSwitch = false;

    public ChatPanel() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        this.setLayout(null);
        putClientProperty("Aqua.backgroundStyle", "vibrantUltraDark");
        putClientProperty("Aqua.windowStyle", "noTitleBar");
        code = new RoundJTextField(200);
        code.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 8));
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
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    code.setText(theCode[0]);
                }).start();
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
        area.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 15));
        area.setOpaque(false);
        StyledDocument doc3 = area.getStyledDocument();
        SimpleAttributeSet center3 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center3, StyleConstants.ALIGN_CENTER);
        doc3.setParagraphAttributes(0, doc3.getLength(), center3, false);
        area.setBackground(new Color(40, 40, 40));
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        areaScroll = new JScrollPane();
        final long[] start = {0};
        final boolean[] running = {false};
        areaScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
                start[0] = System.currentTimeMillis();
            }
        });
        areaScroll.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
                start[0] = System.currentTimeMillis();
                areaScroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 300));
                if (!running[0]) {
                    running[0] = true;
                    new Thread(() -> {
                        System.out.println("start");
                        while ((System.currentTimeMillis() - start[0]) < 1000) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("running");
                        }
                        areaScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
                        areaScroll.repaint();
                        areaScroll.revalidate();
                        area.repaint();
                        area.revalidate();
                        System.out.println("end");
                        running[0] = false;
                    }).start();
                }
            }

        });

        areaScroll.getViewport().setFocusable(false);
        areaScroll.getViewport().setView(area);
        areaScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        areaScroll.setOpaque(false);
        areaScroll.getViewport().setOpaque(false);
        areaScroll.setBackground(new Color(40, 40, 40));
        areaScroll.getVerticalScrollBar().setPreferredSize(new Dimension(5, 300));
        areaScroll.getVerticalScrollBar().setOpaque(false);
        areaScroll.getVerticalScrollBar().setBorder(new EmptyBorder(0, 0, 0, 0));
        areaScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        areaScroll.setBounds(25, 120, 210, 250);
        areaScroll.getVerticalScrollBar().setUnitIncrement(4);
        this.add(areaScroll);
        addLyrics();
        UIManager.setLookAndFeel("org.violetlib.aqua.AquaLookAndFeel");
        type.setBounds(260, 545, 380, 40);
        type.setCaretColor(Color.GREEN);
        this.add(type);
        ImageIcon playIcon = resizeIcon(new ImageIcon(getClass().getResource("/images/play.png")), 40, 40);
        AbstractButton play = makeButton(playIcon);
        play.setBounds(645, 545, 40, 40);

        play.addActionListener(e -> {
            if (chatSwitch) {
                chat.addText(type.getText(), SpotifyPartyPanelChat.FriendName);
                if (!host)
                    cli.writeToServer("chat " + SpotifyPartyPanelChat.FriendName + " " + type.getText());
                else
                    server.TCPServer.sendToClients("chat " + SpotifyPartyPanelChat.FriendName + " " + type.getText(), null);
                type.setText("");
            } else {
                recommendationHandler();
            }
        });
        type.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (chatSwitch) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        chat.addText(type.getText(), SpotifyPartyPanelChat.FriendName);
                        if (!host)
                            cli.writeToServer("chat " + SpotifyPartyPanelChat.FriendName + " " + type.getText());
                        else
                            server.TCPServer.sendToClients("chat " + SpotifyPartyPanelChat.FriendName + " " + type.getText(), null);
                        type.setText("");
                    }
                } else {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        recommendationHandler();

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


        reqScroll = new JScrollPane();
        reqScroll.getViewport().setView(Chat.back);
        Chat.back.setFocusable(false);
        this.setPreferredSize(new Dimension(450, 460));
        reqScroll.setBounds(250, 70, 450, 460);
        reqScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        reqScroll.setOpaque(false);
        reqScroll.getViewport().setOpaque(false);
        reqScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 517));
        reqScroll.getVerticalScrollBar().setOpaque(false);
        reqScroll.getVerticalScrollBar().setBorder(new EmptyBorder(0, 0, 0, 0));
        reqScroll.getVerticalScrollBar().setUnitIncrement(16);
        reqScroll.getVerticalScrollBar().setBackground(new Color(30, 30, 30));
        reqScroll.setVisible(false);
        this.add(reqScroll);

        JScrollPane chatScroll = new JScrollPane();
        chatViewPort = chatScroll.getViewport();
        Chat.chat.setFocusable(false);
        chatScroll.getViewport().setView(Chat.chat);
        this.setPreferredSize(new Dimension(450, 460));
        chatScroll.setBounds(270, 70, 410, 460);
        chatScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        chatScroll.setOpaque(false);
        chatScroll.getViewport().setOpaque(false);
        chatScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 517));
        chatScroll.getVerticalScrollBar().setOpaque(false);
        chatScroll.getVerticalScrollBar().setBorder(new EmptyBorder(0, 0, 0, 0));
        chatScroll.getVerticalScrollBar().setUnitIncrement(16);
        chatScroll.getVerticalScrollBar().setBackground(new Color(30, 30, 30));
        chatScroll.getVerticalScrollBar().setPreferredSize(new Dimension(5, 300));
        chatScroll.setAutoscrolls(true);
        chatViewPort.setAutoscrolls(true);
        this.add(chatScroll);

        JTextPane req = new JTextPane();
        StyledDocument doc4 = req.getStyledDocument();
        SimpleAttributeSet center4 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center4, StyleConstants.ALIGN_CENTER);
        doc4.setParagraphAttributes(0, doc4.getLength(), center4, false);
        req.setForeground(Color.WHITE);
        req.setEditable(false);
        req.setOpaque(false);
        req.setFocusable(false);
        req.setBorder(new EmptyBorder(0, 0, 0, 0));
        req.setText("Song Requests");
        req.setFont(new Font("CircularSpUIv3T-Bold", Font.BOLD, 30));
        req.setBounds(250, 20, 450, 40);
        this.add(req);
        guest.setBorder(new EmptyBorder(0, 0, 0, 0));
        guest.setEditable(false);
        guest.setOpaque(false);
        guest.setFocusable(false);
        guest.setFont(new Font("CircularSpUIv3T-Bold", Font.BOLD, 14));
        guest.setForeground(Color.WHITE);
        guest.setText("0");
        guest.setBounds(10, 30, 24, 24);
        this.add(guest);

        ImageIcon ic = resizeIcon(new ImageIcon(getClass().getResource("/images/logo.png")), 20, 20);
        AbstractButton mode = makeButton(ic);
        mode.setBounds(677, 3, 22, 22);
        mode.addActionListener(e -> {
            if (chatSwitch) {
                Chat.back.setVisible(true);
                Chat.chat.setVisible(false);
                req.setText("Song Requests");
                System.out.println("Requests");
            } else {
                Chat.back.setVisible(false);
                Chat.chat.setVisible(true);
                req.setText("Party Chat");
                System.out.println("The chat");
            }
            chatSwitch = !chatSwitch;
        });
        this.add(mode);
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
                    /*else if(type.getText().trim().toLowerCase().contains("play!:"))
                    {
                        try {
                            String str = type.getText().trim().toLowerCase();
                            str = str.substring(str.indexOf("play!:") + 6).trim();
                            api.playTrack(SpotifyUtils.findSong(type.getText().toLowerCase().trim().replaceAll("[^a-zA-Z0-9]", " ")).getId());
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
                    }*/
                else if (type.getText().trim().toLowerCase().contains("history!")) {
                    for (Track item : SpotifyPlayerHistory.getHistory()) {
                        work = true;
                        try {
                            api.playTrack(SpotifyUtils.getTrack(type.getText().trim()).getId());
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
                            this.chat.addRequest(new RequestTab(item.getId(), SpotifyPartyPanelChat.FriendName));
                    }
                } else {
                    work = true;
                    try {
                        //api.playTrack(SpotifyUtils.getTrackInfo(type.getText().trim()).getId());
                    } catch (Exception e) {
                        String str = type.getText().trim().toLowerCase().replaceAll("[^a-zA-Z0-9]", " ");
                        try {

                            tab = new RequestTab(SpotifyUtils.search(type.getText().trim()).get(0).getUri(), SpotifyPartyPanelChat.FriendName);
                        } catch (Exception e3) {
                            type.setText("can't find that song");
                            type.selectAll();
                            work = false;
                            //api.playTrack(SpotifyUtils.findSong(str).getId());
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
                        TCPServer.sendToClients("request " + tab.toString().split(";")[0].trim() + " " + SpotifyPartyPanelChat.FriendName, null);
                    else
                        cli.writeToServer("request " + tab.toString().split(";")[0] + " " + SpotifyPartyPanelChat.FriendName);
                } catch (Exception e1) {
                    try {
                        tab = new RequestTab(SpotifyUtils.findSong(type.getText().trim()).getId(), SpotifyPartyPanelChat.FriendName);
                    } catch (Exception e3) {
                        type.setText("can't find that song");
                        type.selectAll();
                        work = false;
                    }
                }
            }
            if (work) {
                if (tab != null)
                    chat.addRequest(tab);
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
        if (inf.getName().length() > 33)
            song.setText(inf.getName().substring(0, 30) + " ...");
        else
            song.setText(inf.getName());
        StringBuilder artists = new StringBuilder();
        for (Artist art : inf.getArtists()) {
            artists.append(art.getName() + ", ");
        }
        artists.replace(artists.length() - 2, artists.length(), "");
        if (artists.length() > 38)
            artist.setText(artists.toString().substring(0, 34) + " ...");
        else
            artist.setText(artists.toString());
        color = inf.getDominantColor().darker();
        addLyrics();
        repaint();
        return inf;
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

            //g.drawImage(ImageIO.read(getClass().getResource("/logo.png")), 10, 34, 24, 24, this);
            if (artworkURL != null)
                g.drawImage(ImageIO.read(artworkURL), 55, 400, 140, 140, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {

    }

    @Override
    public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {

    }

    @Override
    public void dragOver(DragSourceDragEvent dragSourceDragEvent) {

    }

    @Override
    public void dropActionChanged(DragSourceDragEvent dragSourceDragEvent) {

    }

    @Override
    public void dragExit(DragSourceEvent dragSourceEvent) {

    }

    @Override
    public void dragDropEnd(DragSourceDropEvent dsde) {

    }
}