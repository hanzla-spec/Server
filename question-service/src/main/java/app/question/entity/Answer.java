package app.question.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "ANSWER")
public class Answer implements Serializable {

    @Id
    @Column(name = "ANSWER_ID", nullable = false)
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

}

