package test;

import coroutines.KThreadRepKt;
import spotifyAPI.OSXSpotifyAPI;
import spotifyAPI.SpotifyWebAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;

public class Test {
    public static void main(String[] args) {
        String[] cmd = {Test.class.getResource("/terminal-notifier-1.7.2/SpotifyParty.app/Contents/MacOS/terminal-notifier").getPath(),
                "-message", "hi", "-title", "nav", "-contentImage", "/Users/naveen/Documents/Idea/SpotifyParty/src/images/SpotifyBG.jpg"};
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

