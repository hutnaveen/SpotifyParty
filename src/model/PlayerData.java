package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerData {
    private Device device;
    private boolean shuffle_state;
    private boolean repeat_state;
    private long timestamp;
    private Context context;
    private long progress_ms;
    private Item item;
    private String currently_playing_type;
    private Actions actions;
    private boolean is_playing;
}
