package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.ArrayList;


public class Chat extends JPanel {
    public static int size = 0;
    public static JTextPane back;
    public static JTextPane chat;
    public static ArrayList<RequestTab> requestTabs = new ArrayList<>();

    public Chat() {
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

    }

    public static void addRequest(RequestTab pane)
    {
        requestTabs.add(pane);
        pane.setBounds(10, 10 +size++ *110, 430, 110);
        chat.add(pane);
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

    public static void addText(String text) {
        StyledDocument doc = chat.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        chat.setFont(new Font("CircularSpUIv3T-Bold", Font.BOLD, 20));
        chat.setForeground(Color.GREEN);
        chat.setText(chat.getText() + "\n" + SpotifyPartyPanelChat.FriendName);

        chat.setFont(new Font("CircularSpUIv3T-Bold", Font.BOLD, 15));
        chat.setForeground(Color.WHITE);
        chat.setText(chat.getText() + "\n" + text + "\n");
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
