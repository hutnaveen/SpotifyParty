package main;

import gui.SpotifyPartyFrame;
import gui.SpotifyPartyPanel;

public class SpotifyParty {
    public static void main(String[] args) {
        System.setProperty("apple.awt.UIElement", "true");
        SpotifyPartyFrame frame = new SpotifyPartyFrame();
        SpotifyPartyPanel panel = new SpotifyPartyPanel();
        frame.add(panel);
    }
}
