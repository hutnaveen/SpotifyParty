package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchItem {
    private Tracks tracks;
}
