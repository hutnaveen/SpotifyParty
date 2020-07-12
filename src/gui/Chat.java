package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import static gui.SpotifyPartyPanelChat.spfc;

public class Chat extends JPanel {
    public static int size = 0;
    public static JTextPane back;
    public static JTextPane chat;
    public static ArrayList<RequestTab> requestTabs = new ArrayList<>();
    public String prev = "";
    private static BufferedImage icon;
    public Chat() {
        try {
            icon = ImageIO.read(Notification.class.getResource("/images/logo.png"));
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        this.setLayout(null);

        this.setLocation(0, 0);
        this.setAutoscrolls(true);

        back = new JTextPane();
        back.setAutoscrolls(true);
        back.setOpaque(false);
        back.setEditable(false);

        chat = new JTextPane();
        chat.setAutoscrolls(true);
        chat.setOpaque(false);
        chat.setEditable(false);
        chat.setMargin(new Insets(10, 10, 0, 10));

    }

    public static void addRequest(RequestTab pane)
    {
        requestTabs.add(pane);
        pane.setBounds(10, 10 +size++ *110, 430, 110);
        back.add(pane);
    }
    public static void redraw(String link) {
        back.removeAll();
        back.setText("");
        size = 0;
        for(int i = 0; i < requestTabs.size(); i++) {
            if(!(requestTabs.get(i).url.equals(link))) {
                requestTabs.get(i).setBounds(10, 70 + size++ *110, 430, 110);
                back.setText(Chat.back.getText() + "\n\n\n\n\n\n\n\n\n\n");
                back.add(requestTabs.get(i));
            } else {
                requestTabs.remove(requestTabs.get(i));
                i--;
            }
        }
    }
    boolean you;
    public void addText(String text, String name) {
        text = reformat(text);

        if(!spfc.isActive()) {
            System.out.println("hi");
            Notification notification = new Notification(icon, "SpotifyParty", name, text, 5000);
            notification.addActivationListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    spfc.toFront();
                    spfc.requestFocus();
                }
            });
            notification.send();
        }


        StyledDocument doc = chat.getStyledDocument();
        SimpleAttributeSet left = new SimpleAttributeSet();
        SimpleAttributeSet right = new SimpleAttributeSet();
        StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
        StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
        // doc.setParagraphAttributes(0, doc.getLength(), left, false);
        chat.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 20));
        Style style = chat.addStyle("I'm a Style", null);
        if(!prev.equals(name)) {
            if(name.equals(SpotifyPartyPanelChat.FriendName)) {
                you = true;
                StyleConstants.setForeground(style, Color.GREEN);
            } else {
                you = false;
                StyleConstants.setForeground(style, Color.GRAY);
            }
            StyleConstants.setFontSize(style, 20);

            try {
                doc.insertString(doc.getLength(), "\n" + name + "\n",style);
                if(you)
                    doc.setParagraphAttributes(doc.getLength() - ("\n" + name + "\n").length(), doc.getLength(), right, false);
                else
                    doc.setParagraphAttributes(doc.getLength() - ("\n" + name + "\n").length(), doc.getLength(), left, false);
            }
            catch (BadLocationException e){}
        }

        StyleConstants.setForeground(style, Color.WHITE);
        StyleConstants.setFontSize(style, 15);

        try {
            doc.insertString(doc.getLength(),  text + "\n",style);
            if(you)
                doc.setParagraphAttributes(doc.getLength() - (text + "\n").length(), doc.getLength(), right, false);
            else
                doc.setParagraphAttributes(doc.getLength() - (text + "\n").length(), doc.getLength(), left, false);
        }
        catch (BadLocationException e){}

        ChatPanel.chatViewPort.setViewPosition(new Point(0, Integer.MAX_VALUE/4));
        prev = name;
    }

    public String reformat(String text) {
        int space = 34;
        while(space < text.length()) {
            int counter = 0;
            while(text.charAt(space) != ' ' && counter < 7) {
                space++;
                counter++;
            }
            counter = 0;
            text = text.substring(0, space) + "\n" + text.substring(space);
            space += 34;
        }
        return text;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            g.drawImage(ImageIO.read(getClass().getResource("/images/SpotifyBG.jpg")), 0, 0, 700, 600, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}