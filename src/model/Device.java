package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Device {
    private String id;
    private String is_active;
    private boolean is_private_session;
    private boolean is_restricted;
    private String name;
    private String type;
    private int volume_percent;
}
