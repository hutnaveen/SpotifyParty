package interfaces;

import exception.SpotifyException;
import model.Artist;
import model.Item;

import java.io.IOException;
import java.util.List;

public interface SpotifyPlayerAPI {
    public  boolean isSingle();
    public void setVolume(int vol);
    public  void playPause() throws IOException;
    public  void play();
    public  void pause();
    public  long getPlayBackPosition() throws SpotifyException, IOException;
    public  void setPlayBackPosition(long pos);
    public long getDuration() throws SpotifyException, IOException;
    public int getVolume() throws SpotifyException, IOException;
    public  boolean isPlaying() throws SpotifyException, IOException;
    public  void nextTrack();
    public  void previousTrack();
    public List<Artist> getTrackArtists() throws IOException;
    public  String getTrackAlbum() throws IOException;
    public  String getTrackUri() throws IOException;
    public  String getTrackName() throws IOException;
    public Item getPlayingTrack() throws IOException;
    public boolean playTrack(String id);
    public boolean playTrack(Item song);
}
