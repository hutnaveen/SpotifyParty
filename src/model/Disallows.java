package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Disallows {
    private boolean resuming;
}
