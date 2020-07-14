package gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;

public class GUIUtilsChat {
    public static AbstractButton makeButton(String title, ImageIcon icon) {
        return new JButton(title) {
            @Override public void updateUI() {
                super.updateUI();
                setVerticalAlignment(SwingConstants.CENTER);
                setVerticalTextPosition(SwingConstants.CENTER);
                setHorizontalAlignment(SwingConstants.CENTER);
                setHorizontalTextPosition(SwingConstants.CENTER);
                setBorder(BorderFactory.createEmptyBorder());
                //setBorderPainted(false);
                setContentAreaFilled(false);
                setFocusPainted(false);
                setOpaque(false);
                setForeground(Color.WHITE);
                setIcon(icon);
            }
        };
    }
    public static AbstractButton makeButton(ImageIcon icon) {
        return makeButton("", icon);
    }
    public static JButton makeButton(String title)
    {
        JButton button = new JButton(title);
        button.setFont(new Font("CircularSpUIv3T-Light", Font.PLAIN, 30));
        button.setForeground(new Color(30, 215, 96));
        return button;
    }

    public static ImageIcon resizeIcon(ImageIcon in, int width, int height)
    {
        Image img = in.getImage();
        Image newimg = img.getScaledInstance(width, height,  Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }
}

class RoundJTextField extends JTextField {
    private Shape shape;
    public RoundJTextField(int size) {
        putClientProperty("Aqua.useFocusRing", "false");
        setBorder(null);
        setOpaque(true);
        setColumns(size);
        setFont(new Font("CircularSpUIv3T-Light",Font.BOLD, 20));
        setPreferredSize(new Dimension(40, 40));
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setCaretColor(Color.BLACK);
        setOpaque(false); // As suggested by @AVD in comment.
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                try {
                    if (isEditable() && e.getKeyCode() == KeyEvent.VK_DELETE && get().isEmpty() || get().isBlank() || (getSelectedText()!= null && getSelectedText().equals(get()))) {
                        e.consume();
                        setText(" ");
                        setCaretPosition(1);
                        //moveCaretPosition(1);
                    } if (isEditable() && e.getKeyCode() == KeyEvent.VK_DELETE) {
                        setText(get().substring(0, get().length() - 1));
                    } if (e.isMetaDown() && e.getKeyCode() == KeyEvent.VK_V) {
                        System.out.println("here");
                        Clipboard clipboard = Toolkit
                                .getDefaultToolkit().getSystemClipboard();
                        try {
                            System.out.println(clipboard.getData(DataFlavor.stringFlavor));
                            setText( (String) clipboard.getData(DataFlavor.stringFlavor));
                        } catch (UnsupportedFlavorException | IOException unsupportedFlavorException) {
                            unsupportedFlavorException.printStackTrace();
                        }

                    }
                }catch (Exception e1)
                {
                    e1.printStackTrace();
                }

            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if(isEditable() && get().isEmpty()|| get().isBlank())
                {
                    e.consume();
                    setText(" ");
                    setCaretPosition(1);
                    //moveCaretPosition(1);
                }
            }
        });
    }
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
        super.paintComponent(g);
    }
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
    }
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 15, 15);
        }
        return shape.contains(x, y);
    }
    private String get()
    {
        return super.getText();
    }
    @Override
    public String getText() {
        return super.getText().trim();
    }
    @Override
    public void setText(String text) {
        super.setText(" " + text);
    }

}

class RoundTextArea extends JTextArea {
    private Shape shape;
    public RoundTextArea() {
        super();
        setOpaque(true); // As suggested by @AVD in comment.
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(isEditable() && e.getKeyCode() == KeyEvent.VK_DELETE && get().isEmpty()|| get().isBlank())
                {
                    System.out.println("up");
                    e.consume();
                    setText(" ");
                    moveCaretPosition(1);
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if(isEditable() && get().isEmpty()|| get().isBlank())
                {
                    e.consume();
                    setText(" ");
                    moveCaretPosition(1);
                }
            }
        });

    }
    private String get()
    {
        return super.getText();
    }
    @Override
    public String getText() {
        return super.getText().trim();
    }
    @Override
    public void setText(String text) {
        super.setText(" " + text);
    }

    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
        super.paintComponent(g);
    }
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
    }
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 15, 15);
        }
        return shape.contains(x, y);
    }
}

class RoundBorder implements Border {

    private int radius;

    public RoundBorder(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, getRadius(), getRadius()));
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        int value = getRadius() / 2;
        return new Insets(value, value, value, value);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}