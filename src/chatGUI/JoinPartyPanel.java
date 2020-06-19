package chatGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static gui.GUIUtil.makeButton;
import static gui.GUIUtil.resizeIcon;

public class JoinPartyPanel extends JPanel {
    AbstractButton enter;

    public JoinPartyPanel() {
        this.setLayout(null);

        ImageIcon icon = resizeIcon(new ImageIcon(getClass().getResource("/slice4.png")), 200, 70);
        enter = makeButton("", icon);
        enter.setBounds(100, 100,200, 70);
        this.add(enter);

        JLabel text = new JLabel("Join Party", SwingConstants.CENTER);
        text.setForeground(Color.WHITE);
        text.setBounds(100, 100, 100, 100);
        this.add(text);

        JTextField code = new JTextField("Party Code");
        code.setForeground(Color.WHITE);
        code.setBounds(200, 200, 200, 200);
        this.add(code);

        JTextField name = new JTextField("Name");
        name.setForeground(Color.WHITE);
        name.setBounds(200, 200, 200, 200);
        this.add(name);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            g.drawImage(ImageIO.read(getClass().getResource("/SpotifyBG.jpg")), 0, 0, 700, 600, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
