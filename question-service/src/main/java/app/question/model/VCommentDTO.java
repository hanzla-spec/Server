package app.question.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VCommentDTO implements Serializable {
    private String commentId;
    private String userId;
    private String answerId;
    private String comment;
    private Long commentedAt;
    private String displayName;
    private String avatar;
    private String badge;
    private Long points;
}
