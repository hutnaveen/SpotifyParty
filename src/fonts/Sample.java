package fonts;

import main.SpotifyParty;
import org.mockito.Mock;

import spotifyAPI.SpotifyAppleScriptWrapper;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Sample {
    public static void main(String[] args) {
        try {
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(SpotifyParty.class.getResource("/fonts/SF-Pro-Display-Bold.otf").getFile())));
            System.out.println(Arrays.toString(ge.getAllFonts()));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

    }
}