package utils;

import model.Track;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class SpotifyUtils{
    public static Track getTrackInfo(String id)
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
                sb.append(sc.next()).append("\n");
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
    public static Track findSong(String search) {
        //Retrieving the contents of the specified page
            search.trim();
            String txt = ("https://songbpm.com/searches/" + search.replace(" ", "-"));
        Document doc = null;
        try {
            doc = Jsoup.connect(txt).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element e = doc.select("a").get(3);
        return getTrackInfo(e.attr("href"));
    }
    public static void main(String[] args) {
        System.out.println(findSong("you need to calm down"));
    }
}
