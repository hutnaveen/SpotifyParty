package utils;

import model.TrackInfo;
import org.apache.commons.lang3.StringEscapeUtils;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class SpotifyUtils{
    public static TrackInfo getTrackInfo(String id)
    {
        String param1 = "";
        String param2 = "";
        TrackInfo info = new TrackInfo();
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
        try {
            sc = new Scanner(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Instantiating the StringBuffer class to hold the result
        StringBuffer sb = new StringBuffer();
        while(sc.hasNext()) {
            sb.append(sc.next()).append("\n");
            //System.out.println(sc.next());
        }
        //Retrieving the String from the String Buffer object
        String result = sb.toString();
        //Removing the HTML tags
        result = result.replaceAll("<[^>]*>", "").replace("\\/", "/");
        int beg = result.indexOf("\"name\":") + 8;
        info.setArtist(result.substring(beg, result.indexOf("\"",beg)).replace("\n", " "));
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

    public static void main(String[] args) {
        String id = "spotify:track:1UNs8AlFM8c0uAkSyjIW3P?si=2smAKjjPQvWVmlv-zRcFTA";
        System.out.println(getTrackInfo(id));
    }
}
