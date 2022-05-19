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

    @Column(name = "IS_ANSWERED")
    private String isAnswered;

    @Column(name = "IS_VOTED")
    private String isVoted;

    @Column(name = "ANSWER_ID")
    private String answerId;
}
