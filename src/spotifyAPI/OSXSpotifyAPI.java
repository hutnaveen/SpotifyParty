package spotifyAPI;

public class OSXSpotifyAPI extends SpotWebSkimmer {
    SpotifyAppleScriptWrapper wrapper;
    public OSXSpotifyAPI(String user, String pass)
    {
        super(user, pass);
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
}
