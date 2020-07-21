package model;
import lombok.Builder;
import lombok.Data;

import java.awt.*;
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
        return Color.decode(dominantColor);
    }
}

