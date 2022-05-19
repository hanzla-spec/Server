package app.question.model;
import lombok.*;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnswerDTO implements Serializable {

    private String answerId;
    private String questionId;
    private String userId;
    private String body;
    private Integer noOfVotes;
    private Long answeredAt;
    private String isEdited;
    private Long editedAt;
    private Integer noOfComments;
}
