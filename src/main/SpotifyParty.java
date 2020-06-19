package main;

import gui.SpotifyPartyFrame;
import gui.SpotifyPartyPanel;

import java.io.*;

/**
 * @author Naveen Govindaraju, Dhaunsh Ramkumar
 */
public class SpotifyParty {
    public static BufferedWriter writer;
    public static void main(String[] args) {
        System.setProperty("apple.awt.UIElement", "true");
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
