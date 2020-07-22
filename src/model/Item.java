package model;
import lombok.Builder;
import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class Item {
    private Album album;
    private List<Artist> artists;
    private int disc_number;
    private long duration_ms;
    private boolean explicit;
    private ExternalIDs external_ids;
    private ExternalURLs external_urls;
    private URL href;
    private String id;
    private boolean is_local;
    private boolean is_playable;
    private String name;
    private int popularity;
    private URL preview_url;
    private int track_number;
    private String type;
    private String uri;
    private String dominantColor;
    public Color getDominantColor()
    {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(getAlbum().getImages().get(2).getUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int x1 = 0 + 1;
        int y1 = 0 + bi.getHeight();
        int sumr = 0, sumg = 0, sumb = 0;
        int num= 0;
        for (int x = 0; x < x1; x++) {
            for (int y = 1; y < y1; y++) {
                Color pixel = new Color(bi.getRGB(x, y));
                sumr += pixel.getRed();
                sumg += pixel.getGreen();
                sumb += pixel.getBlue();
                num++;
            }
        }
        return new Color((sumr / num), sumg / num, sumb / num);
       // return Color.decode(dominantColor);
    }
}

