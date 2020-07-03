package lyrics;

import interfaces.SpotifyPlayerAPI;
import model.SearchResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import spotifyAPI.SpotifyAppleScriptWrapper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class LyricScraper {
    public static void main(String[] args) {
        SpotifyPlayerAPI api = new SpotifyAppleScriptWrapper();
        List<SearchResult> results = DuckDuckGoSearch.getSearchResults(api.getTrackName() + " " + api.getTrackArtist() + " genius lyrics");
        for(SearchResult result: results)
        {
            if(result.getUrl().endsWith("-lyrics") && result.getTitle().endsWith("| Genius Lyrics"))
            {
                System.out.println(getLyrics(result.getUrl()).trim());
                return;
            }
        }
    }

    public static String getLyrics(String song, String artist)
    {
       /* artist = artist.toLowerCase().replace(" ", "-");
        int index = song.indexOf('[');
        if(index != -1)
        {
            song = song.substring(0, index).trim();
        }
        index = song.indexOf("(feat. ");
        if(index != -1)
        {
            song = song.substring(0, song.indexOf("(")).trim();
        }
        song = song.toLowerCase().replace(" ", "-").trim();
        List<Character> list = song.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        for(int i = 0; i < list.size(); i++)
        {
            if(!Character.isLetter(list.get(i)) && !Character.isDigit(list.get(i)) && !(list.get(i) == 45) && list.get(i) != 'í' && list.get(i) != 'é' && list.get(i) != 'ñ'&& list.get(i) != 'ú')
            {
                list.remove(i--);
            }
        }
        Character[] refArray = (Character[]) list.toArray(new Character[list.size()]);
        char[] charArray = new char[refArray.length];
        for (int i = 0; i < refArray.length; i++) {
            charArray[i] = refArray[i];
        }
        song = String.valueOf(charArray).trim();
        System.out.println("https://genius.com/" + artist + "-" + song + "-lyrics");
        try {
            Document doc = Jsoup.connect("https://genius.com/" + artist + "-" + song + "-lyrics").get();
            Element e = doc.getElementsByClass("lyrics").get(0);
            return (e.html().replace("<br>", "\n").replaceAll("<[^>]*>", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;*/
        SpotifyPlayerAPI api = new SpotifyAppleScriptWrapper();
        List<SearchResult> results = DuckDuckGoSearch.getSearchResults(api.getTrackName() + " " + api.getTrackArtist() + " genius lyrics");
        for(SearchResult result: results)
        {
            if(result.getUrl().endsWith("-lyrics") && result.getTitle().endsWith("| Genius Lyrics"))
            {
                return (getLyrics(result.getUrl()).trim());
            }
        }
        return null;
    }
    public static String getLyrics(String url)
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
