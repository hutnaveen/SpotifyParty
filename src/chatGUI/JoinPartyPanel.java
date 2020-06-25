package chatGUI;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import static chatGUI.GUIUtilsChat.makeButton;
import static gui.GUIUtil.resizeIcon;

public class JoinPartyPanel extends JPanel {
    public AbstractButton enter;

    public static boolean one = true;
    public static boolean two = true;

    public static JTextField name;
    public static JTextField code;

    public JoinPartyPanel() {
        this.setLayout(null);

        ImageIcon icon = resizeIcon(new ImageIcon(getClass().getResource("/slice4.png")), 200, 70);
        enter = makeButton("ENTER");
        enter.setBounds(250, 325,200, 70);
        this.add(enter);

        JLabel text = new JLabel("Join Party", SwingConstants.CENTER);
        try {
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(getClass().getResource("/CircularSpUIv3T-Bold.7eb7d0f7.ttf").getFile())));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(getClass().getResource("/CircularSpUIv3T-Light.89e4be2e.ttf").getFile())));
        } catch (IOException |FontFormatException e) {
            //Handle exception
        }
        text.setFont(new Font("CircularSpUIv3T-Bold", Font.PLAIN, 70));
        text.setForeground(Color.WHITE);
        text.setBounds(150, 145, 400, 100);
        this.add(text);

        code = new RoundJTextField(200);
        code.setForeground(Color.GRAY);
        code.setText("Code");
        code.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(two) {
                    super.mousePressed(e);
                    code.setForeground(Color.BLACK);
                    code.setText("");
                    two = !two;
                }
            }
        });
        code.setBounds(355, 250, 200, 50);
        this.add(code);
        name = new RoundJTextField(200);
        name.setForeground(Color.GRAY);
        name.setText("Name");
        name.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(one) {
                    super.mousePressed(e);
                    name.setForeground(Color.BLACK);
                    one = !one;
                }
            }
        });
        name.setBounds(145, 250, 200, 50);
        this.add(name);
        name.setSelectionEnd(0);
        code.setSelectionEnd(0);

    }

    public AbstractButton getEnter() {return enter;}

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            //g.drawImage(ImageIO.read(getClass().getResource("/SpotifyBG.jpg")), 0, 0, 700, 600, this);
            g.drawImage(ImageIO.read(getClass().getResource("/logo.png")), 10, 27, 40, 40, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
