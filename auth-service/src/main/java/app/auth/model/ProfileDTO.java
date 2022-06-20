package app.auth.model;

import lombok.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfileDTO implements Serializable {

    private String profileId;
    private String userId;
    private String username;
    private String displayName;
    private String avatar;
    private String about;
    private Long points;
    private String badge;
    private String website;
    private String achievements;

}
