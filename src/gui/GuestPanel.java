package gui;

import client.TCPClient;
import utils.NetworkUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import static gui.GUIUtil.makeButton;
import static gui.GUIUtil.resizeIcon;

class GuestPanel extends JPanel {
    AbstractButton back;
    JTextField field;
    JLabel textField;
    TCPClient cli;
    public GuestPanel() {
        this.setSize(400, 400);
        this.setLayout(null);
        textField = new JLabel("Enter Your Code", SwingConstants.CENTER);
        textField.setForeground(Color.WHITE);
        field = new RoundJTextField(50);
        field.setBackground(Color.WHITE);
        field.setFont(new Font("SansSerif", Font.BOLD, 20));
        field.setBounds(50, 80, 300, 50);

        textField.setFont(new Font("SansSerif", Font.BOLD, 25));
        textField.setBounds(50, -20, 300, 150);

        ImageIcon icon = resizeIcon(new ImageIcon(getClass().getResource("/slice4.png")), 200, 70);
        AbstractButton enter = makeButton("", icon);
        enter.setBounds(100, 140, 200, 70);

        ImageIcon backImg = resizeIcon(new ImageIcon(getClass().getResource("/backButton.png")), 20, 20);
        back = makeButton("", backImg);
        back.setBounds(15, 15, 20, 20);

        this.add(textField);
        this.add(field);
        this.add(enter);
        this.add(back);
        back.addActionListener(actionEvent -> {
            if(cli != null)
            {
                cli.quit();
                cli = null;
            }

        });
        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //str = textField.getText();
                if(cli == null) {
                    Object[] info = NetworkUtils.simpleDecode(field.getText());
                    if (info == null) {
                        textField.setText("Invalid");
                    } else {
                        cli = new TCPClient((String)info[0], (int)info[1]);
                        textField.setText("Success");
                    }
                }
            }
        });
    }

    public AbstractButton getBack() { return back; }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            g.drawImage(ImageIO.read(getClass().getResource("/SpotifyBG.jpg")), 0, 0, 400, 250, this);
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
