package interfaces;

import exception.SpotifyException;
import model.Track;

import java.awt.image.BufferedImage;
import java.net.URL;

public interface SpotifyPlayerAPI {
    public BufferedImage getArtworkImage();
    public  boolean isSingle();
    public  void increaseVolume(int am);
    public  void decreaseVolume(int am);
    public void setVolume(int am);
    public URL getArtworkURL() throws SpotifyException;
    public  void togglePlay();
    public  void play();
    public  void pause();
    public  long getPlayerPosition() throws SpotifyException;
    public  void setPlayBackPosition(long pos);
    public long getDuration() throws SpotifyException;
    public int getVolume() throws SpotifyException;
    public  boolean isPlaying() throws SpotifyException;
    public  void nextTrack();
    public  void previousTrack();
    public String getTrackArtist();
    public  String getTrackAlbum();
    public  String getTrackId();
    public  String getTrackName();
    public void playTrack(String id);
    public void playTrack(Track song);
    public void saveTrack(String trackId);
}
