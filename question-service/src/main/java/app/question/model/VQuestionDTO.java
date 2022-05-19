package app.question.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VQuestionDTO {

    private String questionId;
    private String title;
    private String body;
    private String tags;
    private Integer noOfVotes;
    private Long askedAt;
    private String isEdited;
    private Long editedAt;
    private Integer noOfAnswers;
    private Integer noOfViews;
    private String userId;
    private String displayName;
    private String avatar;
    private String badge;
    private Long points;
}
