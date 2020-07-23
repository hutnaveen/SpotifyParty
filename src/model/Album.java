package model;

import lombok.Builder;
import lombok.Data;

import java.net.URL;
import java.util.Date;
import java.util.List;
@Data
@Builder
public class Album
{
    String album_type;
    List<Artist> artists;
    ExternalURLs external_urls;
    URL href;
    String id;
    List<Image> images;
    String name;
    String release_date;
    String release_date_precision;
    int total_tracks;
    String type;
    String uri;
}
