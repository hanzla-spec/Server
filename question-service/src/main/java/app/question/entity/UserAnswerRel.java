package app.question.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USER_ANSWER_REL")
public class UserAnswerRel implements Serializable {
    @Id
    @Column(name = "REL_ID", nullable = false)
    private String relId;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "ANSWER_ID", nullable = false)
    private String answerId;

    @Column(name = "IS_ANSWERED" , columnDefinition = "varchar(10) default 'false'")
    private String isAnswered;

    @Column(name = "IS_VOTED", columnDefinition = "varchar(10) default 'false'")
    private String isVoted;

    @Column(name = "IS_COMMENTED" ,columnDefinition = "varchar(10) default 'false'")
    private String isCommented;
}
