package utils;

import com.google.gson.Gson;
import lyrics.DuckDuckGoSearch;
import lyrics.LyricFinder;
import model.SearchItem;
import model.SearchResult;
import model.Item;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static utils.OSXUtils.processToString;

public class SpotifyUtils{
    /*public static Track getTrackInfo(String id)
    {
        String param1 = "";
        String param2 = "";
        Track info = new Track();
        if(id.contains("spotify:")) {
            info.setId(id);
            id = id.replace("spotify:", "");
             param1 = id.substring(0, id.indexOf(":"));
             param2 = id.substring(id.lastIndexOf(":") + 1);
        }
        else if(id.contains("https://open.spotify.com/"))
        {
            info.setId(id.replace("https://open.spotify.com/", "spotify:").replace("/", ":"));
            id = id.replace("https://open.spotify.com/", "");
            param1 = id.substring(0, id.indexOf("/"));
            param2 = id.substring(id.lastIndexOf("/") + 1);
        }
        URL url = null;
        try {
            url = new URL("https://open.spotify.com/embed/" + param1 +"/" + param2);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Retrieving the contents of the specified page
        Scanner sc = null;
        StringBuilder sb = new StringBuilder();
        try {
            assert url != null;
            sc = new Scanner(url.openStream());

            //Instantiating the StringBuffer class to hold the result
            while (true) {
                assert sc != null;
                if (!sc.hasNext()) break;
                sb.append(sc.next()).append(" ");
                //System.out.println(sc.next());
            }
        }
        catch (Exception e ) {
            return null;
        }
        //Retrieving the String from the String Buffer object
        String result = sb.toString();
        //Removing the HTML tags
        result = result.replaceAll("<[^>]*>", "").replace("\\/", "/");
        result = result.substring(result.indexOf("\"playback-control.skip-back\":\"Previous\",\"playback-control.skip-forward\":\"Next\"}") + 79);
        System.out.println(result);
        int beg = result.indexOf("\"name\":") + 8;
        info.setArtist(result.substring(beg, result.indexOf("\"",beg)).replace("\n", " "));
        info.setArtist(StringEscapeUtils.unescapeJava(info.getArtist()));
        beg = result.lastIndexOf("\"name\":") + 8;
        info.setName(result.substring(beg, result.indexOf("\"",beg)).replace("\n", " "));
        info.setName(StringEscapeUtils.unescapeJava(info.getName()));
        beg = result.lastIndexOf("\"height\":300,\"url\":\"") + 20;
        try {
            info.setThumbnailURL(new URL(result.substring(beg, result.indexOf("\"",beg))));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        beg = result.lastIndexOf("\"dominantColor\":\"") + 17;
        info.setDominantColor(Color.decode(result.substring(beg, result.indexOf("\"",beg))));
        beg = result.lastIndexOf("\"popularity\":") + 13;
        info.setPopularity(Integer.parseInt(result.substring(beg, result.indexOf(",",beg))));
        return info;
    }
*/
    public static String getLyrics(Item song)
    {
        return LyricFinder.getLyrics(song.getName(),(song.getArtists().get(0).getName()));
    }
    private static String getTrackJson(String id) {
        String param1 = "";
        String param2 = "";
        if (id.contains("spotify:")) {
            id = id.replace("spotify:", "");
            param1 = id.substring(0, id.indexOf(":"));
            param2 = id.substring(id.lastIndexOf(":") + 1);
        } else if (id.contains("https://open.spotify.com/")) {
            id.replace("https://open.spotify.com/", "spotify:").replace("/", ":");
            id = id.replace("https://open.spotify.com/", "");
            param1 = id.substring(0, id.indexOf("/"));
            param2 = id.substring(id.lastIndexOf("/") + 1);
        }
        URL url = null;
        try {
            url = new URL("https://open.spotify.com/embed/track/" + param2);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Retrieving the contents of the specified page
        Scanner sc = null;
        StringBuilder sb = new StringBuilder();
        try {
            assert url != null;
            sc = new Scanner(url.openStream());

            //Instantiating the StringBuffer class to hold the result
            while (true) {
                assert sc != null;
                if (!sc.hasNext()) break;
                sb.append(sc.next()).append(" ");
                //System.out.println(sc.next());
            }
        } catch (Exception e) {
            return null;
        }
        //Retrieving the String from the String Buffer object
        String result = sb.toString();
        //Removing the HTML tags
        result = result.replaceAll("<[^>]*>", "").replace("\\/", "/");
        result = result.substring(result.lastIndexOf("{\"album\""));
        return result;
    }

    public static void main(String[] args) {

    }
}
