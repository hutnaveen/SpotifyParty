package chatGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static gui.GUIUtil.makeButton;
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
        enter = makeButton("", icon);
        enter.setBounds(250, 325,200, 70);
        this.add(enter);

        JLabel text = new JLabel("Join Party", SwingConstants.CENTER);
        text.setFont(new Font("Proxima Nova", Font.BOLD, 50));
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
                    name.setText("");
                    one = !one;
                }
            }
        });
        name.setBounds(145, 250, 200, 50);
        this.add(name);
    }

    public AbstractButton getEnter() {return enter;}

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            g.drawImage(ImageIO.read(getClass().getResource("/SpotifyBG.jpg")), 0, 0, 700, 600, this);
            g.drawImage(ImageIO.read(getClass().getResource("/logo.png")), 10, 10, 40, 40, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
