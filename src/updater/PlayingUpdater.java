package updater;

import main.SpotifyParty;
import model.PlayerData;
import utils.TimeUtils;

public class PlayingUpdater {

    public PlayingUpdater(PlayerData sentData)
    {
        if (sentData.is_playing())
            SpotifyParty.api.play();
        else
            SpotifyParty.api.pause();
    }
}
