package app.question.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "V_QUESTION")
@Immutable
@Subselect(value = "select q.question_id,q.title,q.body,q.tags,q.no_of_votes,q.asked_at,q.is_edited," +
        "q.edited_at,q.no_of_answers,q.no_of_views,p.user_id,p.display_name,p.avatar,p.badge,p.points " +
        "from question q left join " +
        "profile p on q.user_id = p.user_id ")
@Synchronize({ "QUESTION", "PROFILE" })
public class VQuestion implements Serializable {

    @Id
    @Column(name = "QUESTION_ID")
    private String questionId;

    @Column(name = "TITLE",length = 500)
    private String title;

    @Column(name = "BODY",columnDefinition = "TEXT")
    private String body;

    @Column(name = "TAGS")
    private String tags;

    @Column(name = "NO_OF_VOTES")
    private Integer noOfVotes;

    @Column(name = "ASKED_AT")
    private Long askedAt;

    @Column(name = "IS_EDITED")
    private String isEdited;

    @Column(name = "EDITED_AT")
    private Long editedAt;

    @Column(name = "NO_OF_ANSWERS")
    private Integer noOfAnswers;

    @Column(name = "NO_OF_VIEWS")
    private Integer noOfViews;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "DISPLAY_NAME")
    private String displayName;

    @Column(name = "AVATAR")
    private String avatar;

    @Column(name = "BADGE")
    private String badge;

    @Column(name = "POINTS")
    private Long points;

}
