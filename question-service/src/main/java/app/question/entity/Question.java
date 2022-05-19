package app.question.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "QUESTION")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Question implements Serializable{
    @Id
    @Column(name = "QUESTION_ID",nullable = false)
    private String questionId;

    @Column(name = "USER_ID",nullable = false)
    private String userId;

    @Column(name = "TITLE", nullable = false, length = 500)
    private String title;

    @Column(name = "BODY", nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "TAGS")
    private String tags;

    @Column(name = "NO_OF_VOTES")
    private Integer noOfVotes;

    @Column(name = "ASKED_AT", nullable = false)
    private Long askedAt;

    @Column(name = "IS_EDITED")
    private String isEdited;

    @Column(name = "EDITED_AT")
    private Long editedAt;

    @Column(name = "NO_OF_ANSWERS")
    private Integer noOfAnswers;

    @Column(name = "NO_OF_VIEWS")
    private Integer noOfViews;
}
