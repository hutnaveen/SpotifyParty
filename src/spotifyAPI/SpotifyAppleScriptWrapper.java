package spotifyAPI;

import interfaces.SpotifyPlayerAPI;
import model.Track;
import utils.OSXUtils;
import utils.SpotifyUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SpotifyAppleScriptWrapper implements SpotifyPlayerAPI {
    public void playTrack(String id){
        try {
            OSXUtils.runAppleCmd("tell application \"Spotify\"\n" +
                    "     play track \""+id+"\"\n" +
                    "end tell");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playTrack(Track song) {
        playTrack(song.getId());
    }


    @Override
    public void saveTrack(String trackId) {

    }

    public BufferedImage getArtworkImage()
    {
        try {
            return ImageIO.read(getArtworkURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean isSingle()
    {
       /* String str = getTrackInfo();
        assert str != null;
        if(str.contains("\"album_type\":\"single\""))
            return true;*/
        return false;
    }
    public  void increaseVolume(int am)
    {
        try {
            OSXUtils.runAppleCmd("tell application \"Spotify\"\n" +
                    "\tset currentvol to get sound volume\n" +
                    "\tif currentvol > "+ (100-am)+" then\n" +
                    "\t\tset sound volume to 100\n" +
                    "\telse\n" +
                    "\t\tset sound volume to currentvol + "+am+"\n" +
                    "\tend if\n" +
                    "end tell");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  void decreaseVolume(int am)
    {
        try {
            OSXUtils.runAppleCmd("tell application \"Spotify\"\n" +
                    "    set currentvol to get sound volume\n" +
                    "    -- volume wraps at 100 to 0\n" +
                    "    if currentvol < " + am +" then\n" +
                    "        set sound volume to 0\n" +
                    "    else\n" +
                    "        set sound volume to currentvol - "+am+"\n" +
                    "    end if\n" +
                    "end tell\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setVolume(int am) {
    }

    public  URL getArtworkURL()
    {
        return SpotifyUtils.getTrackInfo(getTrackId()).getThumbnailURL();
    }
    public  void togglePlay()
    {
        try {
            OSXUtils.runAppleCmd("\n" +
                    "tell application \"Spotify\"\n" +
                    "\tplaypause\n" +
                    "end tell\n" +
                    "\n" +
                    "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  void play()
    {
        try {
            OSXUtils.runAppleCmd("\n" +
                    "tell application \"Spotify\"\n" +
                    "\tplay\n" +
                    "end tell\n" +
                    "\n" +
                    "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public  void pause()
    {
        try {
            OSXUtils.runAppleCmd("\n" +
                    "tell application \"Spotify\"\n" +
                    "\tpause\n" +
                    "end tell\n" +
                    "\n" +
                    "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public long getPlayerPosition()
    {
        try {
            String str = OSXUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\treturn player position as text\n" +
                    "end tell");
            return (long)(Double.parseDouble(str)) * 1000;
        } catch (NumberFormatException e) {
            return 0L;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0l;
    }
    public  void setPlayBackPosition(long pos)
    {
        try {
            OSXUtils.runAppleCmd("tell application \"Spotify\"\n" +
                    "\tset player position to "+ pos/1000 +"\n" +
                    "end tell");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public long getDuration()
    {
        try {
            return Long.parseLong(OSXUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\tduration of the current track\n" +
                    "end tell").trim().replace("\"", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getVolume() {
        return 0;
    }

    public  boolean isPlaying()
    {
        try {
           String str =  OSXUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\treturn player state\n" +
                    "end tell").trim();
           if(str.startsWith("pl"))
                return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public  void nextTrack()
    {
        try {
            OSXUtils.runAppleCmd("tell application \"Spotify\"\n" +
                    "\tnext track\n" +
                    "end tell");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  void previousTrack()
    {
        try {
            OSXUtils.runAppleCmd("tell application \"Spotify\"\n" +
                    "\tprevious track\n" +
                    "end tell");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getTrackArtist()
    {
        try {
            return OSXUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\tartist of current track\n" +
                    "end tell").trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
   /* public  java.util.List<String> getTrackArtists()
    {
        java.util.List<String> names = new ArrayList<>();
        String str = getTrackInfo();
        int pos1 = str.indexOf("a song by") + 10;
        String ret = str.substring(pos1, str.indexOf(" on Spotify", pos1));
        for(String s: ret.split(", "))
            names.add(s);
        return names;
    }*/
    public  String getTrackAlbum()
    {
        try {
            return OSXUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\talbum of current track\n" +
                    "end tell");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
       /*String s = getTrackInfo();
       int pos1 = s.indexOf("alt=") + 5;
       return s.substring(pos1, s.indexOf('\"', pos1)).trim().replace("&#039;", "'");*/
    }
    public  String getTrackId()
    {
        try {
            return OSXUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\treturn spotify url of the current track\n" +
                    "end tell").trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getTrackName()
    {
       /*String str = getTrackInfo();
       int pos = str.indexOf("name\":") + 7;
       return str.substring(pos, str.indexOf("\"",pos)).replace("\\u2019","'").replace("\\", "");*/
        try {
            return OSXUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\tname of current track\n" +
                    "end tell").trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getTrackInfo()
    {


       /* try {
            return OSXUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\tset long_id to id of current track\n" +
                    "\tset AppleScript's text item delimiters to \":\"\n" +
                    "\tset short_id to long_id's third text item\n" +
                    "\tset _art to do shell script \"curl -s -X GET 'https://open.spotify.com/track/\" & short_id & \"' \"\n" +
                    "end tell\n" +
                    "\n" +
                    "\n" +
                    "return _art");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return null;
    }
}
