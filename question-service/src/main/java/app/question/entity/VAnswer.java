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
@Table(name = "V_ANSWER")
@Immutable
@Subselect(value = "select a.answer_id,a.question_id,a.user_id,a.body,a.no_of_votes,a.answered_at,a.is_edited," +
        "a.edited_at,a.no_of_comments,p.display_name,p.avatar,p.badge,p.points " +
        "from answer a left join " +
        "profile p on a.user_id = p.user_id ")
@Synchronize({ "ANSWER", "PROFILE" })
public class VAnswer implements Serializable {
    @Id
    @Column(name = "ANSWER_ID")
    private String answerId;

    @Column(name = "QUESTION_ID", nullable = false)
    private String questionId;

    @Column(name = "USER_ID",nullable = false)
    private String userId;

    @Column(name = "BODY", nullable = false,columnDefinition = "TEXT")
    private String body;

    @Column(name = "NO_OF_VOTES")
    private Integer noOfVotes;

    @Column(name = "ANSWERED_AT", nullable = false)
    private Long answeredAt;

    @Column(name = "IS_EDITED")
    private String isEdited;

    @Column(name = "EDITED_AT")
    private Long editedAt;

    @Column(name = "NO_OF_COMMENTS")
    private Integer noOfComments;

    @Column(name = "DISPLAY_NAME")
    private String displayName;

    @Column(name = "AVATAR")
    private String avatar;

    @Column(name = "BADGE")
    private String badge;

    @Column(name = "POINTS")
    private Long points;
}
