package app.question.model;
import lombok.*;

import java.io.Serializable;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserQuestionRelDTO implements Serializable {

    private String relId;
    private String userId;
    private String questionId;
    private String isAnswered;
    private String isVoted;
    private String answerId;
}
