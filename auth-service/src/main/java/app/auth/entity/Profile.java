package app.auth.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "PROFILE")
public class Profile implements Serializable {

    @Id
    @Column(name = "PROFILE_ID", nullable = false)
    private String profileId;

    @Column(name = "USER_ID", nullable = false,unique = true)
    private String userId;

    @Column(name = "USERNAME", nullable = false,unique = true)
    private String username;

    @Column(name = "DISPLAY_NAME", nullable = false)
    private String displayName;

    @Column(name = "AVATAR")
    private String avatar;

    @Column(name = "ABOUT",length = 500)
    private String about;

    @Column(name = "POINTS")
    private Long points;

    @Column(name = "BADGE")
    private String badge;

    @Column(name ="WEBSITE")
    private String website;

    @Column(name = "ACHIEVEMENTS", length = 1000)
    private String achievements;
}
