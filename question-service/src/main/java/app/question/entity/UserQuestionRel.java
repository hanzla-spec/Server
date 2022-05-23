package app.question.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USER_QUESTION_REL")
public class UserQuestionRel implements Serializable {
    @Id
    @Column(name = "REL_ID", nullable = false)
    private String relId;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "QUESTION_ID", nullable = false)
    private String questionId;

    @Column(name = "IS_ASKED", columnDefinition = "varchar(10) default 'false'")
    private String isAsked;

    @Column(name = "IS_VOTED", columnDefinition = "varchar(10) default 'false'")
    private String isVoted;
}
