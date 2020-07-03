package lyrics;

import interfaces.SpotifyPlayerAPI;
import spotifyAPI.SpotifyAppleScriptWrapper;
import javax.swing.*;
import java.awt.*;

public class Main {
    private static JTextPane area = new JTextPane();
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
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
        frame.getRootPane().putClientProperty("Aqua.backgroundStyle", "vibrantUltraDark");
       // frame.getRootPane().putClientProperty("Aqua.windowStyle", "transparentTitleBar");
        frame.getRootPane().putClientProperty("Aqua.windowTopMargin", "0");
        JPanel panel = new JPanel();
        SpotifyPlayerAPI api = new SpotifyAppleScriptWrapper();
        /*StyledDocument doc = area.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);*/
        panel.putClientProperty("Aqua.windowStyle", "transparentTitleBar");
        JScrollPane pane = new JScrollPane();
        pane.getViewport().setView(area);
        area.setEditable(false);
        panel.add(pane);
        frame.add(panel);
        frame.setVisible(true);
        panel.putClientProperty("Aqua.backgroundStyle", "vibrantUltraDark");
        area.setForeground(Color.BLACK);
        area.setText(LyricFinder.getLyrics(api.getTrackName(), api.getTrackArtist()));
    }

}
