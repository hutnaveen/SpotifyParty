package gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static main.SpotifyParty.defFont;
import static utils.GUIUtils.makeButton;
import static utils.GUIUtils.resizeIcon;

public class ChooseParty extends JPanel {
    AbstractButton join;
    AbstractButton host;

    public ChooseParty() {
        this.setLayout(null);
        try {
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();

            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(getClass().getResource("/fonts/CircularSpUIv3T-Bold.7eb7d0f7.ttf").getFile())));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(getClass().getResource("/fonts/CircularSpUIv3T-Light.89e4be2e.ttf").getFile())));
        } catch (IOException |FontFormatException e) {}

        JLabel title = new JLabel("Spotify Party!", SwingConstants.CENTER);
        title.setFont(new Font(defFont, Font.PLAIN, 65));
        title.setForeground(Color.WHITE);
        title.setBounds(45, 145, 600, 200);
        this.add(title);

        ImageIcon icon = resizeIcon(new ImageIcon(getClass().getResource("/images/slice2.png")), 170, 75);
        join = makeButton(icon);
        join.setBounds(165, 305,170, 75);
        this.add(join);

        ImageIcon icon2 = resizeIcon(new ImageIcon(getClass().getResource("/images/slice1.png")), 170, 75);
        host = makeButton(icon2);
        host.setBounds(345, 305,170, 75);
        this.add(host);

        JLabel load2 = new JLabel("Loading...", SwingConstants.CENTER);
        load2.setFont(new Font(defFont, Font.PLAIN, 30));
        load2.setForeground(Color.WHITE);
        load2.setBounds(350, 307, 160, 70);
        this.add(load2);
    }

    public AbstractButton getJoin() {return join;}
    public AbstractButton getHost() {return host;}

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            g.drawImage(ImageIO.read(getClass().getResource("/images/logo.png")), 10, 27, 40, 40, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
