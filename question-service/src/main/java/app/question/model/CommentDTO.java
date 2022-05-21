package app.question.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private String commentId;
    private String userId;
    private String answerId;
    private String comment;
    private Long commentedAt;
}
