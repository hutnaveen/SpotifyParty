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
        SpotifyPartyFrame frame = new SpotifyPartyFrame();
        SpotifyPartyPanel panel = new SpotifyPartyPanel();
        frame.add(panel);
        try
        {
            File ayy = new File(System.getProperty("user.home") + "/Documents/log.txt");
            if(!ayy.exists())
                ayy.createNewFile();
            writer  = new BufferedWriter(new FileWriter(ayy, true));
        }catch (IndexOutOfBoundsException e)
        {
           writer = null;
        } catch (IOException e) {
            writer = null;
        }
    }
}
