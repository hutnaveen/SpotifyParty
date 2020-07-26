package spotifyAPI;

import model.Item;

public class OSXSpotifyAPI extends SpotifyWebAPI {
    SpotifyAppleScriptWrapper wrapper;
    public OSXSpotifyAPI() {
        super();
       wrapper = new SpotifyAppleScriptWrapper();
    }
    public OSXSpotifyAPI(String token) {
        super(token);
        wrapper = new SpotifyAppleScriptWrapper();
    }
    @Override
    public void nextTrack() {
        wrapper.nextTrack();
    }

    @Override
    public void previousTrack() {
        wrapper.previousTrack();
    }

    @Override
    public void pause() {
        wrapper.pause();
    }

    @Override
    public void play() {
        wrapper.play();
    }

    @Override
    public void setPlayBackPosition(long pos) {
        wrapper.setPlayBackPosition(pos);
    }

    @Override
    public void playPause() {
        wrapper.playPause();
    }
    public boolean playTrack(String id)
    {
        return wrapper.playTrack(id);
    }

    @Override
    public boolean playTrack(Item song) {
        return false;
    }
}
