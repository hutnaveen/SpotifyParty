package fonts;

import org.mockito.Mock;

import spotifyAPI.SpotifyAppleScriptWrapper;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Sample {
    public static void main(String[] args) {
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(Sample.class.getResource("/fonts/Arial Unicode MS.ttf").getFile())));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
           // e.printStackTrace();
        }
        System.out.println(Arrays.toString(ge.getAllFonts()));
    }
}