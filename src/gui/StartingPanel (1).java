package gui;/*
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static gui.GUIUtil.makeButton;

class StartingPanel extends JPanel {

    public StartingPanel() {
        this.setLayout(new GridLayout(2, 1, 0, 0));
        setBackground(new Color(40,40,40));
        AbstractButton join = makeButton("", new ImageIcon("slice1.png"));
        AbstractButton host = makeButton("",new ImageIcon("slice2.png"));

        join.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: next Panel
                SpotifyPartyPanel.cl.show(SpotifyPartyPanel.spp, "guest");
            }
        });
        host.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: next Panel
                SpotifyPartyPanel.cl.show(SpotifyPartyPanel.spp, "host");
            }
        });

        this.add(join);
        this.add(host);


    }


}
*/
