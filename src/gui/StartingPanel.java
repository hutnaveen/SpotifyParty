package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import static gui.GUIUtil.makeButton;
import static gui.GUIUtil.resizeIcon;

class StartingPanel extends JPanel {
    ImageIcon joinIcon = resizeIcon(new ImageIcon(getClass().getResource("/slice1.png")), 185, 80);
    ImageIcon hostIcon = resizeIcon(new ImageIcon(getClass().getResource("/slice2.png")), 185, 80);
    AbstractButton join = makeButton("", joinIcon);
    AbstractButton host = makeButton("", hostIcon);

    public AbstractButton getJoin() {
        return join;
    }

    public void setJoin(AbstractButton join) {
        this.join = join;
    }

    public AbstractButton getHost() {
        return host;
    }

    public void setHost(AbstractButton host) {
        this.host = host;
    }

    public StartingPanel() {
        JLabel text = new JLabel("Spotify Party!", SwingConstants.CENTER);
        text.setFont(new Font("DialogInput", Font.BOLD, 50));
        text.setBounds(25, 0, 350, 160);
        text.setForeground(Color.WHITE);

        setBackground(new Color(40,40,40));
        this.setLayout(null);
        join.setBounds(10, 150, 185, 80);
        host.setBounds(210, 150, 185, 80);

        join.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: next Panel

            }
        });
        host.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: next Panel
            }
        });

        this.add(join);
        this.add(host);
        this.add(text);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            g.drawImage(ImageIO.read(getClass().getResource("/SpotifyBG.jpg")), 0, 0, 400, 250, this);
            //g.drawImage(ImageIO.read(getClass().getResource("/logo.png")), 10, 10, 25, 25, this);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Image getImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
