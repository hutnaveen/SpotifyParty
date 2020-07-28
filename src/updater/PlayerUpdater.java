package updater;
import model.PlayerData;
import utils.TimeUtils;

import java.io.IOException;

import static main.SpotifyParty.api;

public class PlayerUpdater {
    public PlayerUpdater(PlayerData sentData) {
        System.out.println("called");
        try {
            long timestamp = System.currentTimeMillis();
            PlayerData data = api.getPlayerData();
            sentData.setTimestamp(timestamp);
            if (sentData!= null && data != null && data.getItem() != null) {
                    if (!data.getItem().getUri().equals(sentData.getItem().getUri())) {
                        new URIUpdater(sentData);
                    }
                    if (Math.abs((System.currentTimeMillis() - sentData.getTimestamp()) + sentData.getProgress_ms() - data.getProgress_ms()) > 1500) {
                        new ProgressUpdater(sentData);
                    }
                    if (data.is_playing() != data.is_playing()) {
                       new PlayingUpdater(sentData);
                    }
                    //System.out.println(data.getTimestamp());
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
