package fonts;

import org.mockito.Mock;

import spotifyAPI.SpotifyAppleScriptWrapper;

import java.awt.*;
import java.util.Arrays;

public class Sample {
    public static void main(String[] args) {
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        System.out.println(Arrays.toString(ge.getAllFonts()));
    }
}