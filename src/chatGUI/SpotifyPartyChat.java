package chatGUI;

import gui.SpotifyPartyFrame;
import gui.SpotifyPartyPanel;
import utils.OSXUtils;

import javax.swing.*;
import java.io.IOException;

/**
 * @author Naveen Govindaraju, Dhaunsh Ramkumar
 */
public class SpotifyPartyChat {
    public static boolean darkMode = true;
    public static void main(String[] args) {
        System.setProperty("apple.awt.UIElement", "true");
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
        try {
            darkMode = OSXUtils.runAppleScript("tell application \"System Events\"\n" +
                    "\treturn dark mode of appearance preferences\n" +
                    "end tell").startsWith("tru");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SpotifyPartyPanelChat panel = new SpotifyPartyPanelChat();



    }
}
