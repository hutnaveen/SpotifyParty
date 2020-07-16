package webAPI;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import com.wrapper.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;
import lombok.SneakyThrows;
import java.net.URI;
import java.net.URISyntaxException;

public class Test {
    static SpotifyApi spotifyApi;
    static
    {
        try {
            spotifyApi = new SpotifyApi.Builder()
                        .setClientId("0537c6bb3b054367af2c339bc1bc63d4")
                        .setClientSecret("29e5faa34ca047ecb7a33e621426a140")
                        .setRedirectUri(new URI("https://github.com/mithulg/Flow"))
                        .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        final GetUsersCurrentlyPlayingTrackRequest currentlyPlaying = spotifyApi.getUsersCurrentlyPlayingTrack().build();
        CurrentlyPlaying track = currentlyPlaying.execute();
        track.getContext().getHref();
    }
}
