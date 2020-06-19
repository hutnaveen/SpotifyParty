package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import static gui.GUIUtil.makeButton;
import static gui.GUIUtil.resizeIcon;

public class HostPanel extends JPanel {
    AbstractButton back;
    JTextField field;
    public HostPanel() {
        this.setSize(400, 400);
        this.setLayout(null);

        JLabel textField = new JLabel("Send This Code", SwingConstants.CENTER);
        textField.setForeground(Color.WHITE);
        field = new RoundJTextField(50);
        field.setBounds(50, 80, 300, 50);
        field.setEditable(false);

        field.setFont(new Font("SansSerif", Font.BOLD, 20));

        textField.setFont(new Font("SansSerif", Font.BOLD, 25));
        textField.setBounds(50, -20, 300, 150);

        ImageIcon icon = resizeIcon(new ImageIcon(getClass().getResource("/slice3.png")), 200, 70);
        AbstractButton copy = makeButton("", icon);
        copy.setBounds(100, 140, 200, 70);

        ImageIcon backImg = resizeIcon(new ImageIcon(getClass().getResource("/backButton.png")), 20, 20);
        back = makeButton("", backImg);
        back.setBounds(15, 15, 20, 20);
        this.add(textField);
        this.add(field);
        this.add(copy);
        this.add(back);

        copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringSelection selection = new StringSelection(getCode());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            }
        });
    }

    public AbstractButton getBack() { return back; }

    public String getCode() {
        return field.getText();
    }

    public void setCode(String code) {
        field.setText(code);
    }

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