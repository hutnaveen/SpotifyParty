package chatGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class Chat extends JPanel implements Scrollable {
    private int size = 0;
    private JScrollPane scroll;

    public Chat() {
        this.setLayout(null);

        this.setLocation(250, 0);
        this.setSize(450, 520);

        scroll = new JScrollPane();
        scroll.setBounds(0, 0, 450, 520);
        scroll.setBorder(new EmptyBorder(0,0,0,0));
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 520));
        this.add(scroll);
        revalidate();
    }

    public void addRequest(JPanel pane)
    {
        pane.setBounds(10, 10 + size++ *85, 430, 80);
        //this.add(pane);
        scroll.add(pane);
        //scroll.revalidate();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try
        {
            g.drawImage(ImageIO.read(getClass().getResource("/SpotifyBG.jpg")), 0, 0, 700, 600, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return null;
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 0;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 0;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
