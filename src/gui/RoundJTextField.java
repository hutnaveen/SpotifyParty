package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

import static main.SpotifyParty.defFont;

public class RoundJTextField extends JTextField {
    private Shape shape;
    public int type;
    public RoundJTextField(int size, int round) {
        type = round;
        putClientProperty("Aqua.useFocusRing", "false");
        putClientProperty("JTextField.style", "round");
        setBorder(new EmptyBorder(new Insets(4,4,4,4)));
        setOpaque(true);
        setColumns(size);
        setFont(new Font(defFont,Font.BOLD, 20));
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
                    setCaretPosition(2);
                    //moveCaretPosition(1);
                }
            }
        });
    }
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, type, type);
        super.paintComponent(g);
    }
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, type, type);
    }
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, type, type);
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
