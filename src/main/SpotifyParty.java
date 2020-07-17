package main;

import gui.ChatPanel;
import gui.SpotifyPartyPanelChat;
import utils.OSXUtils;

import javax.swing.*;
import java.io.IOException;

/**
 * @author Naveen Govindaraju, Dhaunsh Ramkumar
 */
public class SpotifyParty {
    public static ChatPanel chatPanel = null;
    public static boolean darkMode = true;
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        System.setProperty("apple.awt.UIElement", "true");
        chatPanel = new ChatPanel();
            UIManager.setLookAndFeel("org.violetlib.aqua.AquaLookAndFeel");
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
