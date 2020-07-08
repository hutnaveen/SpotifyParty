package model;


import lombok.Builder;
import lombok.Data;

import java.net.URL;
@Builder
@Data
public class Artist
{
    ExternalURLs url;
    URL href;
    String id;
    String name;
    String type;
    String uri;
}