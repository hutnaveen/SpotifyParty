package lyrics;
import model.SearchResult;
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
        List<SearchResult> results = DuckDuckGoSearch.getSearchResults(song + " " + artist + " genius lyrics");
        for(SearchResult result: results)
        {
            if(result.getUrl().endsWith("-lyrics") && result.getTitle().endsWith("| Genius Lyrics"))
            {
                return (getLyrics(result.getUrl()).trim());
            }
        }
        return null;
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
