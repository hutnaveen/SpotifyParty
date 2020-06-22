package chatGUI;

import exception.SpotifyException;
import interfaces.SpotifyPlayerAPI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

import static gui.GUIUtil.makeButton;
import static gui.GUIUtil.resizeIcon;

public class ChatPanel extends JPanel {
    private SpotifyPlayerAPI api;
    public static ArrayList<String> names = new ArrayList<>();
    public static AbstractButton copy;

    public ChatPanel() {
        names.add("Bella Hadid");
        names.add("Travis");
        names.add("Hrithik");
        names.add("Ritvik");
        names.add("The Weeknd");
        names.add("XXX");
        names.add("Jay Sean");
        names.add("Bella Hadid");
        names.add("Travis");
        names.add("Hrithik");
        names.add("Ritvik");
        names.add("The Weeknd");
        names.add("XXX");
        names.add("Jay Sean");
        this.setLayout(null);

        JTextField code = new RoundJTextField(200);
        code.setForeground(Color.GRAY);
        code.setText("Code");
        code.setBounds(40, 10, 195, 30);
        code.setEditable(false);
        this.add(code);

        JLabel text = new JLabel("Friends", SwingConstants.CENTER);
        text.setFont(new Font("Proxima Nova", Font.BOLD, 30));
        text.setForeground(Color.WHITE);
        text.setBounds(-70, 20, 400, 100);
        this.add(text);

        JTextPane area = new JTextPane();
        area.setAutoscrolls(true);
        area.setEditable(false);
        area.setForeground(Color.WHITE);
        area.setFont(new Font("Proxima Nova", Font.BOLD, 13));
        area.setText(getNames());
        StyledDocument doc = area.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        area.setBackground(Color.BLACK);
        JScrollPane areaScroll = new JScrollPane(area);
        areaScroll.setBackground(Color.BLACK);
        areaScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 300));
        areaScroll.setBounds(25, 100, 200, 300);
        this.add(areaScroll);
    }

    public String getNames() {
        String str = "";
        for(int i = 0; i < names.size(); i++) {
            str = str + (" " + names.get(i) + "\n\n");
        }
        return str;
    }

    public URL getAlbumCover() throws SpotifyException {
        return api.getArtworkURL();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            g.drawImage(ImageIO.read(getClass().getResource("/SpotifyBG.jpg")), 0, 0, 700, 600, this);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 250, 600);
            g.drawImage(ImageIO.read(getClass().getResource("/logo.png")), 10, 15, 20, 20, this);
            g.setColor(Color.WHITE);
            g.drawLine(30, 90, 220, 90);
            g.drawImage(ImageIO.read(getAlbumCover()), 10, 400, 30, 30, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
