package app.auth.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USER")
@Getter
@Setter
public class User implements Serializable {
    @Id
    @Column(name = "ID", nullable = false)
    private String Id;

    @Column(name = "USERNAME", nullable = false,unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ROLE", nullable = false)
    private String role;
}
