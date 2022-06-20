package app.auth.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse implements Serializable {

    private String type;
    private String access_token;
    private String refresh_token;
    private Long expiresInMillis;
    private String userId;
    private String isVerified;

}
