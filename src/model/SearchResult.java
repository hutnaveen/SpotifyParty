package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class SearchResult {
    private String url;
    private String title;
    private String snippet;
}
