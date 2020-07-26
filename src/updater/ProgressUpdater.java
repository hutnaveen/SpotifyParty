package updater;

import main.SpotifyParty;
import model.PlayerData;
import utils.TimeUtils;

public class ProgressUpdater {
    ProgressUpdater(PlayerData data, PlayerData sentData)
    {
        System.out.println("Time: " + sentData.getProgress_ms() + " Player: " + data.getProgress_ms());
        SpotifyParty.api.setPlayBackPosition(sentData.getProgress_ms() + (TimeUtils.getAppleTime() - sentData.getTimestamp()) + 500);
    }

}
