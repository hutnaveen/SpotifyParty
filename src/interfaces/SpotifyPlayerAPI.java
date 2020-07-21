package interfaces;

import exception.SpotifyException;
import model.Artist;
import model.Item;

import java.util.List;

public interface SpotifyPlayerAPI {
    public  boolean isSingle();
    public void setVolume(int vol);
    public  void playPause();
    public  void play();
    public  void pause();
    public  long getPlayBackPosition() throws SpotifyException;
    public  void setPlayBackPosition(long pos);
    public long getDuration() throws SpotifyException;
    public int getVolume() throws SpotifyException;
    public  boolean isPlaying() throws SpotifyException;
    public  void nextTrack();
    public  void previousTrack();
    public List<Artist> getTrackArtists();
    public  String getTrackAlbum();
    public  String getTrackUri();
    public  String getTrackName();
    public Item getPlayingTrack();
    public boolean playTrack(String id);
    public boolean playTrack(Item song);
}
