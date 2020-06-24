package chatGUI;

import gui.SpotifyPartyFrame;
import gui.SpotifyPartyPanel;

import javax.swing.*;

/**
 * @author Naveen Govindaraju, Dhaunsh Ramkumar
 */
public class SpotifyPartyChat {
    public static void main(String[] args) {
        //System.setProperty("apple.awt.UIElement", "true");
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
        SpotifyPartyPanelChat panel = new SpotifyPartyPanelChat();



    }
}
