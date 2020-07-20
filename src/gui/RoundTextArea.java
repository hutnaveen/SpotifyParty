package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RoundTextArea extends JTextArea {
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
