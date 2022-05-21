package app.question.service;

import app.question.entity.Comment;
import app.question.entity.UserAnswerRel;
import app.question.entity.VComment;
import app.question.model.CommentDTO;
import app.question.repository.CommentRepository;
import app.question.repository.UserAnswerRelRepository;
import app.question.repository.VCommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserAnswerRelRepository userAnswerRelRepository;

    @Autowired
    private VCommentRepository vCommentRepository;

    public void postComment(CommentDTO commentDTO) {
        commentDTO.setCommentId(UUID.randomUUID().toString());
        commentDTO.setCommentedAt(System.currentTimeMillis());
        ModelMapper mapper = new ModelMapper();
        Comment comment = mapper.map(commentDTO, new TypeToken<Comment>(){}.getType());
        commentRepository.save(comment);

        log.info("performing operation to update user answer rel db");

        //TODO: check and update the same entry
        Optional<UserAnswerRel> userAnswerRelDB = userAnswerRelRepository.findUserAnswerRel(comment.getUserId(), commentDTO.getAnswerId());
        UserAnswerRel userAnswerRel = null;
        if(userAnswerRelDB.isPresent()){
            userAnswerRel = userAnswerRelDB.get();
            userAnswerRel.setIsCommented("true");
            userAnswerRelRepository.save(userAnswerRel);
        }else{
            userAnswerRel = new UserAnswerRel();
            userAnswerRel.setRelId(UUID.randomUUID().toString());
            userAnswerRel.setAnswerId(commentDTO.getAnswerId());
            userAnswerRel.setUserId(commentDTO.getUserId());
            userAnswerRel.setIsCommented("true");
            userAnswerRelRepository.save(userAnswerRel);
        }
    }

    public List<VComment> getVCommentsOfAnswer(String answerId) {
        return vCommentRepository.findByAnswerId(answerId);
    }
}
