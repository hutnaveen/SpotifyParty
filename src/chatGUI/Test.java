package chatGUI;

import org.violetlib.aqua.AquaCustomStyledWindow;
import org.violetlib.aqua.AquaInternalFramePaneUI;
import org.violetlib.aqua.AquaTextFieldUI;
import org.violetlib.aqua.AquaTextPaneUI;

import javax.swing.*;
import java.awt.*;

public class Test
{
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("org.violetlib.aqua.AquaLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame();
        frame.getRootPane().putClientProperty("Aqua.windowStyle","noTitleBar");
        frame.setSize(500, 500);
        JPanel panel = new JPanel();
        frame.add(panel);
        JTextField pn = new JTextField(30);
        panel.add(pn);
        frame.setVisible(true);


    }
}