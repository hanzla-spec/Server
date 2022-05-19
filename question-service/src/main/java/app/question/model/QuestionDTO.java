package app.question.model;

import lombok.*;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionDTO implements Serializable {

    private String questionId;
    private String userId;
    private String title;
    private String body;
    private String tags;
    private Integer noOfVotes;
    private Long askedAt;
    private String isEdited;
    private Long editedAt;
    private Integer noOfAnswers;
    private Integer noOfViews;
}
