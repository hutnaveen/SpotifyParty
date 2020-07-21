package model;

import lombok.Builder;
import lombok.Data;

import java.net.URL;

@Builder
@Data
public class Context {
    private ExternalURLs external_urls;
    private URL href;
    String type;
    String uri;
}
