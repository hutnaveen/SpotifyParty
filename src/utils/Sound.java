package utils;
import java.net.URL;

public class Sound{
    static {
        //JFXPanel fxPanel = new JFXPanel();
    }
    public static synchronized void playSound(final URL path) {
       /* new Thread(() -> {
            final Media media = new Media(path.toString());
            final MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        }).start();*/
    }
}
