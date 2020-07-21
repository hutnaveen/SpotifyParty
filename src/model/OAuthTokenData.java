package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OAuthTokenData {
    private String access_token;
    private String token_type;
    private long expires_in;
    private String refresh_token;
}
