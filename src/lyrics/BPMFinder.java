package lyrics;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import spotifyAPI.SpotifyAppleScriptWrapper;
import utils.OSXUtils;
import utils.SpotifyUtils;

import java.io.IOException;

public class BPMFinder {
    public static void main(String[] args) {
        System.out.println(getBPM(new SpotifyAppleScriptWrapper().getTrackName(), new SpotifyAppleScriptWrapper().getTrackArtist()));
    }
    public static int getBPM(String song, String artist)
    {
        song = song.toLowerCase();
        artist = artist.toLowerCase().trim();
        int index = song.indexOf("(");
        if (index != -1)
            song = song.substring(0, index).trim();
        index = song.indexOf("feat.");
        if (index != -1)
            song = song.substring(0, index).trim();
        try {
            String txt = ("https://songbpm.com/searches/" + song.replace(" ", "-") +"-"+ artist.replace(" ", "-")).replace("---", "-").replace("--", "-");
            Document doc = Jsoup.connect(txt).get();
            Element e = doc.getElementsByClass("text-center").get(2);
            String text = e.text().trim();
            return Integer.parseInt(text.substring(0, text.indexOf(" ")).trim());
        } catch (Exception e) {
            return -1;
        }
    }

}
