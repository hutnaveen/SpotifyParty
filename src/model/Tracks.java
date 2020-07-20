package model;

import lombok.Builder;
import lombok.Data;

import java.net.URL;
import java.util.List;

@Data
@Builder
public class Tracks {
    private URL href;
    private List<Track> items;
    private int limit;
    private URL next;
    private int offset;
    private String previous;
    private int total;
}
