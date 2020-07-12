package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class Requests extends JPanel {
    public static ArrayList<RequestTab> requestTabs = new ArrayList<>();
    public JScrollPane reqScroll;
    public static JTextPane back;
    public static int size = 0;

    public Requests() {
        this.setLayout(null);

        back = new JTextPane();
        back.setAutoscrolls(true);
        back.setOpaque(false);
        back.setEditable(false);
        back.setFocusable(false);

        reqScroll = new JScrollPane();
        reqScroll.getViewport().setView(back);
        //this.setPreferredSize(new Dimension(450, 460));
        reqScroll.setBounds(0, 0, 450, 460);
        reqScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        reqScroll.setOpaque(false);
        reqScroll.getViewport().setOpaque(false);
        reqScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 517));
        reqScroll.getVerticalScrollBar().setOpaque(false);
        reqScroll.getVerticalScrollBar().setBorder(new EmptyBorder(0, 0, 0, 0));
        reqScroll.getVerticalScrollBar().setUnitIncrement(16);
        reqScroll.getVerticalScrollBar().setBackground(new Color(30, 30, 30));
        reqScroll.setVisible(false);
        this.add(reqScroll);
    }

    public static void addRequest(RequestTab pane)
    {
        requestTabs.add(pane);
        pane.setBounds(10, 10 +size++ *110, 430, 110);
        back.add(pane);
    }

    public static void redraw(String link) {
        back.removeAll();
        back.setText("");
        size = 0;
        for(int i = 0; i < requestTabs.size(); i++) {
            if(!(requestTabs.get(i).url.equals(link))) {
                requestTabs.get(i).setBounds(10, 70 + size++ *110, 430, 110);
                back.setText(back.getText() + "\n\n\n\n\n\n\n\n\n\n");
                back.add(requestTabs.get(i));
            } else {
                requestTabs.remove(requestTabs.get(i));
                i--;
            }
        }
    }
}
