package updater;
import model.PlayerData;
import utils.TimeUtils;

import java.io.IOException;

import static main.SpotifyParty.api;

public class PlayerUpdater {
    public PlayerUpdater(PlayerData sentData) {
        try {
            PlayerData data = api.getPlayerData();
            if (sentData!= null && data != null) {
                    if (!data.getItem().getUri().equals(sentData.getItem().getUri())) {
                        new URIUpdater(sentData);
                    }
                    if (data.is_playing() != data.is_playing()) {
                       new PlayingUpdater(sentData);
                    }
                    if (Math.abs((TimeUtils.getAppleTime() - data.getTimestamp()) + sentData.getProgress_ms() - data.getProgress_ms()) > 2000) {
                        new ProgressUpdater(data, sentData);
                    }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
