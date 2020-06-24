package chatGUI;

import org.violetlib.aqua.AquaInternalFramePaneUI;
import org.violetlib.aqua.AquaTextFieldUI;
import org.violetlib.aqua.AquaTextPaneUI;

import javax.swing.*;

public class Test
{
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        JPanel panel = new JPanel();
        frame.add(panel);
        //AquaTextFieldUI basic = new AquaTextFieldUI();
        JTextField pn = new JTextField(30);
       // pn.setUI(basic);
        panel.add(pn);
        frame.setVisible(true);


    }
}