package model;

import lombok.Builder;
import lombok.Data;

import java.net.URL;
import java.util.List;

@Data
@Builder
public class UserData {
    private String country;
    private String display_name;
    private String email;
    private ExplicitContent explicit_content;
    private ExternalURLs external_urls;
    private Followers followers;
    private URL href;
    private String id;
    private List<Image> images;
    private String product;
    private String type;
    private String uri;
}
