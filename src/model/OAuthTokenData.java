package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OAuthTokenData {
    private final String access_token;
    private final String token_type;
    private final long expires_in;
    private final String refresh_token;
}
