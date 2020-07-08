package lyrics;
import model.SearchResult;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class LyricFinder {

    public static String getLyrics(String song, String artist, boolean special)
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
            return "well shit it looks like we dont have lyrics for this song or our api just be trippin this shit is open source so shit be like that";

        }
        return "well shit it looks like we dont have lyrics for this song or our api just be trippin this shit is open source so shit be like that";
    }
    public static String getLyrics(String song, String artist)
    {
        String tSong = song;
        String tArtist = song;
        try {
            tArtist = tArtist.toLowerCase().replace(" ", "-");
            int index = song.indexOf('[');
            if (index != -1) {
                tSong = tSong.substring(0, index).trim();
            }
            index = tSong.indexOf("(feat. ");
            if (index != -1) {
                tSong = tSong.substring(0, tSong.indexOf("(")).trim();
            }
            tSong = tSong.toLowerCase().replace(" ", "-").trim();
            List<Character> list = tSong.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
                if (!Character.isLetter(list.get(i)) && !Character.isDigit(list.get(i)) && !(list.get(i) == 45) && list.get(i) != ':') {
                    list.remove(i--);
                }
            }
            Character[] refArray = (Character[]) list.toArray(new Character[list.size()]);
            char[] charArray = new char[refArray.length];
            for (int i = 0; i < refArray.length; i++) {
                charArray[i] = refArray[i];
            }
            song = String.valueOf(charArray).trim();
            String url = ("https://genius.com/" + tArtist + "-" + tSong + "-lyrics");
            return getLyrics(url.replace(":", "-")).replace("---", "-").replace("--", "-");
        }
        catch (Exception e)
        {
            return getLyrics(song, artist, true);
        }
    }
    private static String getLyrics(String url)
    {
        try {
            Document doc = Jsoup.connect(url).get();
            Element e = doc.getElementsByClass("lyrics").get(0);
            return StringEscapeUtils.unescapeHtml4((e.html().replace("<br>", "\n").replaceAll("<[^>]*>", ""))).trim().replace("  ", " ");
        } catch (IOException e) {
            return "well shit it looks like we dont have lyrics for this song or our api just be trippin this shit is open source so shit be like that";
        }
    }
}
