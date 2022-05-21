package app.question.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "V_COMMENT")
@Immutable
@Subselect(value = "select c.comment_id,c.user_id,c.answer_id,c.comment,c.commented_at,p.display_name,p.avatar,p.badge,p.points " +
        "from comment c left join " +
        "profile p on c.user_id = p.user_id ")
@Synchronize({ "COMMENT", "PROFILE" })
public class VComment implements Serializable {

    @Id
    @Column(name = "COMMENT_ID", nullable = false)
    private String commentId;

    @Column(name = "USER_ID" , nullable = false)
    private String userId;

    @Column(name = "ANSWER_ID", nullable = false)
    private String answerId;

    @Column(name = "COMMENT" , length = 500)
    private String comment;

    @Column(name = "COMMENTED_AT")
    private Long commentedAt;

    @Column(name = "DISPLAY_NAME")
    private String displayName;

    @Column(name = "AVATAR")
    private String avatar;

    @Column(name = "BADGE")
    private String badge;

    @Column(name = "POINTS")
    private Long points;
}
