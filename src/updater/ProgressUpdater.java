package updater;

import main.SpotifyParty;
import model.PlayerData;
import utils.TimeUtils;

public class ProgressUpdater {
    ProgressUpdater(PlayerData sentData)
    {
        //System.out.println("Time: " + sentData.getProgress_ms() + " Player: " + data.getProgress_ms());
        System.out.println(sentData.getTimestamp());
        SpotifyParty.api.setPlayBackPosition((System.currentTimeMillis() - sentData.getTimestamp()) + sentData.getProgress_ms() + 500);
    }

}
