package chatGUI;

import gui.Notification;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static chatGUI.SpotifyPartyPanelChat.spfc;
import static main.SpotifyParty.defFont;

public class Chat extends JPanel {
    public static JTextPane chat;
    private static BufferedImage icon;
    public JViewport chatViewPort;

    public Chat() {
       this.setOpaque(false);

        putClientProperty("Aqua.backgroundStyle", "vibrantUltraDark");
        try {
            icon = ImageIO.read(Notification.class.getResource("/images/logo.png"));
        } catch (
                IOException e) {
            e.printStackTrace();
        }
       this.setLayout(null);
        putClientProperty("JScrollPane.style", "overlay");
        chat = new JTextPane();
        chat.setOpaque(false);
        if(System.getProperty("os.name").contains("Windows"))
        {
            setBackground(new Color(30,30,30));
            chat.setOpaque(true);
            chat.setBackground(new Color(30,30,30));
            setBackground(new Color(30, 30, 30));
            setOpaque(true);
        }
        chat.setFocusable(false);
        chat.setAutoscrolls(true);
        chat.setEditable(false);
        JScrollPane chatScroll = new JScrollPane();
        chatScroll.putClientProperty("JScrollPane.style", "overlay");
        chat.putClientProperty("JScrollPane.style", "overlay");
        chatViewPort = chatScroll.getViewport();
        chatScroll.getViewport().setView(chat);
        chatScroll.setBounds(20, 0, 405, 460);
        chatScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        chatScroll.setOpaque(false);
        chatScroll.getViewport().setOpaque(false);
        chatScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 517));
        chatScroll.getVerticalScrollBar().setOpaque(false);
        chatScroll.getVerticalScrollBar().setBorder(new EmptyBorder(0, 0, 0, 0));
        chatScroll.getVerticalScrollBar().setUnitIncrement(16);
        chatScroll.getVerticalScrollBar().setBackground(new Color(30, 30, 30));
        chatScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 300));
        chatScroll.setAutoscrolls(true);
        chatViewPort.setAutoscrolls(true);
        this.add(chatScroll);
    }

    public String prev = "";
    public boolean you;
    public void addText(String text, String name) {
        text = reformat(text);
        if(!spfc.isActive()) {
            System.out.println("hi");
            Notification notification = new Notification(icon, "SpotifyParty", name, text, 5000);
            notification.addActivationListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    spfc.setVisible(true);
                    spfc.toFront();
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
        chat.setFont(new Font(defFont, Font.PLAIN, 20));
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

        chatViewPort.setViewPosition(new Point(0, Integer.MAX_VALUE/4));
        prev = name;
    }

    public static String reformat(String text) {
        if(text.length() > 34) {
            StringBuilder ret = new StringBuilder();
            String str[] = text.split(" ");
            StringBuilder temp = new StringBuilder();
            String next = null;
            for (int i = 0; i < str.length; i++) {
                if(next != null) {
                    temp.append(next);
                    next = null;
                }
                if(temp.length() < 34 - str[i].length())
                    temp.append(str[i]).append(" ");
                else {
                    ret.append(temp.toString().trim()).append("\n");
                    temp = new StringBuilder();
                    if (str[i].length() > 34) {
                        StringBuilder builder = new StringBuilder(str[i]);
                        for(int a = 0; a < builder.length()/34 + 1; a++)
                            builder.insert(a*34,"\n");
                        ret.append(builder.toString().trim()).append(" ");
                    }
                    else
                        next = str[i]+ " ";
                }
            }
            if(next != null)
                return (ret.toString().trim()+ ("\n") + (temp)).trim() + ("\n") + (next).trim();
            else
                return (ret.toString().trim() + ("\n") + (temp)).trim();
        }else
            return text;
    }

    private static String resize(String text)
    {
       /* StringBuilder builder = new StringBuilder(text);
        for(int i = 0; i < builder.length()/34 + 1; i++)
            builder.insert(i*34,"\n");
        return builder.toString().trim();*/
        return null;
    }
}