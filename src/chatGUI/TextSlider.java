package chatGUI;

import javax.swing.*;
import java.awt.*;


public class TextSlider {

    public static void main(String args[]) {
        JFrame frame = new JFrame("Text Slider");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JTextPane textField = new JTextPane();

        JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL);

        JPanel panel = new JPanel();
        scrollBar.setMaximum(100);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(textField);
        scrollBar.addAdjustmentListener(e -> System.out.println(e.getValue()));
        panel.add(scrollBar);
        frame.add(panel, BorderLayout.NORTH);
        frame.setSize(300, 100);
        frame.setVisible(true);
    }
}








