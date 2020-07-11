package fonts;

import gui.Notification;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
          BufferedImage icon = null;
            try {
                icon = ImageIO.read(Notification.class.getResource("/images/logo.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
         Notification notif1  = new Notification(icon, "SpotifyParty", "Banakar", "im excited to drive");
         notif1.send();
    }
}
