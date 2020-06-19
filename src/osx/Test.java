package osx;

import spotifyAPI.SpotifyAppleScriptWrapper;

public class Test {
    public static void main(String[] args) {
        System.out.println(new SpotifyAppleScriptWrapper().getTrackInfo());
    }
}
