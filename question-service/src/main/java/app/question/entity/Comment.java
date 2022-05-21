package app.question.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "COMMENT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment implements Serializable {

    @Id
    @Column(name = "COMMENT_ID", nullable = false)
    private String commentId;

    @Column(name = "USER_ID" , nullable = false)
    private String userId;

    @Column(name = "ANSWER_ID", nullable = false)
    private String answerId;

    @Column(name = "COMMENT" , length = 500)
    private String comment;

    @Column(name = "COMMENTED_AT")
    private Long commentedAt;
}
