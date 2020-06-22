package main;

import gui.SpotifyPartyFrame;
import gui.SpotifyPartyPanel;

import javax.swing.*;
import java.io.*;

/**
 * @author Naveen Govindaraju, Dhaunsh Ramkumar
 */
public class SpotifyParty {
    public static BufferedWriter writer;
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        System.setProperty("apple.awt.UIElement", "true");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        SpotifyPartyFrame frame = new SpotifyPartyFrame();
        SpotifyPartyPanel panel = new SpotifyPartyPanel();
        frame.add(panel);
        try
        {
            writer  = new BufferedWriter(new FileWriter(args[0], true));
        }catch (IndexOutOfBoundsException e)
        {
           writer = null;
        } catch (IOException e) {
            writer = null;
        }
    }
}
