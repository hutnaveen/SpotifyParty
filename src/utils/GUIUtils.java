package utils;

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
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GUIUtils {
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
    public static int getTextWidth(String text, Font font)
    {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        return (int)(font.getStringBounds(text, frc).getWidth());
    }
    public static int getTextHeight(String text, Font font)
    {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        return  (int)(font.getStringBounds(text, frc).getHeight());
    }
    public static BufferedImage circleCrop(BufferedImage in)
    {
        int width = in.getWidth();
        BufferedImage circleBuffer = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();
        g2.setClip(new Ellipse2D.Float(0, 0, width, width));
        g2.drawImage(in, 0, 0, width, width, null);
        return circleBuffer;
    }

}

