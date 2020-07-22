package model;

import lombok.Builder;
import lombok.Data;

import java.net.URL;

@Data
@Builder
public class Followers {
    private URL href;
    private int total;

}
