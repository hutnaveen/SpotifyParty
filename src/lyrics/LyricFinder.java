package lyrics;
import model.SearchResult;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import spotifyAPI.SpotifyAppleScriptWrapper;

import java.io.IOException;
import java.util.List;

public class LyricFinder {
    public static void main(String[] args) {
        SpotifyAppleScriptWrapper api = new SpotifyAppleScriptWrapper();
        System.out.println(getLyrics(api.getTrackName().trim(), api.getTrackArtist().trim()));
    }
    public static String getLyrics(String song, String artist)
    {
        try {
            List<SearchResult> results = DuckDuckGoSearch.getSearchResults(song + " " + artist + " genius lyrics");
            for (SearchResult result : results) {
                if (result.getUrl().endsWith("-lyrics") && result.getTitle().endsWith("| Genius Lyrics")) {
                    return (StringEscapeUtils.unescapeHtml4(getLyrics(result.getUrl()).trim()));
                }
            }
        }catch (Exception e)
        {
           // System.err.println(e.getMessage());
            return "well shit it looks like we dont have lyrics for this song\n or out api just be trippin \n this shit is open source so shit be like that";
        }
        return "well shit it looks like we dont have lyrics for this song\n or out api just be trippin \n this shit is open source so shit be like that";
    }
    public static String getLyrics(String song, String artist, boolean special)
    {
            if (special)
                return getLyrics(song.replaceAll("[^a-zA-Z0-9]", " "), artist);
            else
                return getLyrics(song, artist);
    }
    private static String getLyrics(String url)
    {
        try {
            Document doc = Jsoup.connect(url).get();
            Element e = doc.getElementsByClass("lyrics").get(0);
            return (e.html().replace("<br>", "\n").replaceAll("<[^>]*>", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
