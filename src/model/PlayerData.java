package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerData {
    private long timestamp;
    private Context context;
    private long progress_ms;
    private Item item;
    private String currently_playing_type;
    private Actions actions;
    private boolean is_playing;
}
