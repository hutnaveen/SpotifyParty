package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SpotifyUtils {
    public static String[] getTrackinfo(String id)
    {
        String[] str = new String[4];
        URL trackinfo = null;
        try {
            trackinfo = new URL("https://open.spotify.com/oembed?url=" + id);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    trackinfo.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String info = null;
        try {
            info = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(info);
        return null;
    }
}
