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

        JTextField code = new RoundJTextField(200);
        code.setForeground(Color.GRAY);
        code.setText("Code");
        code.setForeground(Color.BLACK);
        code.setBounds(130, 250, 200, 50);
        this.add(code);

        JTextField name = new RoundJTextField(200);
        name.setForeground(Color.GRAY);
        name.setText("Name");
        name.setForeground(Color.BLACK);
        name.setBounds(340, 250, 200, 50);
        this.add(name);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            g.drawImage(ImageIO.read(getClass().getResource("/logo.png")), 10, 10, 40, 40, this);
            g.drawImage(ImageIO.read(getClass().getResource("/SpotifyBG.jpg")), 0, 0, 700, 600, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
