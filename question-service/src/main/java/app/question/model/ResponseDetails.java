package app.question.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDetails implements Serializable {

    private Integer message_code; // 1 for success operation , -1 for failed operation
    private String message;
}
