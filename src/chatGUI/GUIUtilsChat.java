package chatGUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

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
        super(size);
        setOpaque(false); // As suggested by @AVD in comment.
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

class RoundTextArea extends JTextArea {
    private Shape shape;
    public RoundTextArea() {
        super();
        setOpaque(true); // As suggested by @AVD in comment.
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



