package main;

import gui.RoundJTextField;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import static main.SpotifyParty.defFont;
import static utils.GUIUtils.makeButton;
import static utils.GUIUtils.resizeIcon;

public class SignUp extends JPanel {
    public static JTextField pass;
    public static JTextField email;
    public AbstractButton enter;

    public SignUp() {
        this.setLayout(null);

        JLabel text = new JLabel("Sign up", SwingConstants.CENTER);
        try {
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();

            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(getClass().getResource("/fonts/CircularSpUIv3T-Bold.7eb7d0f7.ttf").getFile())));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(getClass().getResource("/fonts/CircularSpUIv3T-Light.89e4be2e.ttf").getFile())));
        } catch (IOException |FontFormatException e) {
            //Handle exception
        }
        text.setFont(new Font(defFont, Font.PLAIN, 55));
        text.setForeground(Color.WHITE);
        text.setBounds(145, 145, 400, 100);
        this.add(text);

        email = new RoundJTextField(400);
        email.setForeground(Color.GRAY);
        email.setText("Email");
        email.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    email.setForeground(Color.BLACK);
                    email.setText("");
            }
        });
        email.setBounds(145, 250, 400, 50);
        this.add(email);

        pass = new RoundJTextField(400);
        pass.setForeground(Color.GRAY);
        pass.setText("Password");
        pass.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    pass.setForeground(Color.BLACK);
                    pass.setText("");
            }
        });
        pass.setBounds(145, 310, 400, 50);
        this.add(pass);

        ImageIcon icon = resizeIcon(new ImageIcon(getClass().getResource("/images/slice4.png")), 180, 60);
        enter = makeButton(icon);
        enter.setBounds(250, 375,180, 60);
        this.add(enter);

        JLabel load = new JLabel("Loading...", SwingConstants.CENTER);
        load.setFont(new Font(defFont, Font.PLAIN, 30));
        load.setForeground(Color.WHITE);
        load.setBounds(255, 380, 170, 55);
        this.add(load);
    }

    public AbstractButton getEnter() {return enter;}

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
