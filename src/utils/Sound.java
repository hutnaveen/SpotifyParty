package utils;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class Sound{
    static {
        JFXPanel fxPanel = new JFXPanel();
    }
    public static synchronized void playSound(final URL path) {
        new Thread(() -> {
            final Media media = new Media(path.toString());
            final MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        }).start();
    }
}
