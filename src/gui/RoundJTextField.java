package gui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;

import static main.SpotifyParty.defFont;

public class RoundJTextField extends JTextField {
    private Shape shape;
    public RoundJTextField(int size) {
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
                    setCaretPosition(1);
                    //moveCaretPosition(1);
                }
            }
        });
    }
    public RoundJTextField(int a, int b)
    {
        super();
        setPreferredSize(new Dimension(a, b));
    }

    @Override protected void paintComponent(Graphics g) {
        if (!isOpaque() && getBorder() instanceof GRoundTextField.RoundedCornerBorder) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(getBackground());
            g2.fill(((GRoundTextField.RoundedCornerBorder) getBorder()).getBorderShape(
                    0, 0, getWidth() - 1, getHeight() - 1));
            g2.dispose();
        }
        super.paintComponent(g);
    }
    @Override public void updateUI() {
        super.updateUI();
        setOpaque(false);
        setBorder(new RoundedCornerBorder());
    }

    class RoundedCornerBorder extends AbstractBorder {
        private final Color ALPHA_ZERO = new Color(0x0, true);
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape border = getBorderShape(x, y, width - 1, height - 1);
            g2.setPaint(ALPHA_ZERO);
            Area corner = new Area(new Rectangle2D.Double(x, y, width, height));
            corner.subtract(new Area(border));
            g2.fill(corner);
            g2.setPaint(Color.WHITE);
            g2.draw(border);
            g2.dispose();
        }

        public Shape getBorderShape(int x, int y, int w, int h) {
            int r = h; //h / 2;
            return new RoundRectangle2D.Double(x, y, w, h, r, r);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 8, 4, 8);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.set(4, 8, 4, 8);
            return insets;
        }
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
