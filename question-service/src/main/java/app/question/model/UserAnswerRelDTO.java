package app.question.model;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserAnswerRelDTO implements Serializable {

    private String relId;
    private String userId;
    private String answerId;
    private String isAnswered;
    private String isVoted;
    private String isCommented;

}

