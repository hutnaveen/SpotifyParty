package utils;

import model.TrackInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SpotifyUtils{
    public static TrackInfo getTrackInfo(String id)
    {
        String title = "";
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
        int beg = info.indexOf("title\":\"") + 8;
        title = info.substring(beg, info.indexOf("\"", beg + 2));
        String thumb = "";
        beg = info.indexOf("\"thumbnail_url\":\"") + 17;
        thumb = info.substring(beg, info.indexOf("\"", beg + 2));
        URL url = null;
        try {
            url = new URL(thumb);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new TrackInfo(id, title, url);
    }

    public static void main(String[] args) {
        System.out.println(getTrackInfo("spotify:track:39Yp9wwQiSRIDOvrVg7mbk?context=spotify%3Aplaylist%3A37i9dQZF1DWY4xHQp97fN6"));
    }
}
