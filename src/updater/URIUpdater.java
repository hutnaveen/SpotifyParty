package updater;

import main.SpotifyParty;
import model.PlayerData;
import utils.TimeUtils;

import java.net.URI;

import static main.SpotifyParty.chatPanel;

public class URIUpdater {

    public URIUpdater(PlayerData sentData)
    {
        System.out.println("trying to play " + sentData.getItem().getName());
        SpotifyParty.api.playTrack(sentData.getItem().getUri());
        try {
            //System.out.println("update to " + api.getTrackInfo(trackID));
            chatPanel.updateData(sentData.getItem().getUri());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(sentData.getProgress_ms());
        SpotifyParty.api.setPlayBackPosition((System.currentTimeMillis() - sentData.getTimestamp()) + sentData.getProgress_ms() + 1000);
    }
}
