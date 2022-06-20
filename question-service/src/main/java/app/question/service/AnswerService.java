package app.question.service;

import app.question.entity.Answer;
import app.question.entity.UserAnswerRel;
import app.question.entity.VAnswer;
import app.question.model.AnswerDTO;
import app.question.model.UserAnswerRelDTO;
import app.question.repository.AnswerRepository;
import app.question.repository.UserAnswerRelRepository;
import app.question.repository.VAnswerRepository;
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
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private VAnswerRepository vAnswerRepository;

    @Autowired
    private UserAnswerRelRepository userAnswerRelRepository;

    public String postAnswer(AnswerDTO answerDTO){
        Answer answer = new Answer();
        String answerId = UUID.randomUUID().toString();
        answer.setAnswerId(answerId);
        answer.setBody(answerDTO.getBody());
        answer.setQuestionId(answerDTO.getQuestionId());
        answer.setUserId(answerDTO.getUserId());
        Long currentMillis = System.currentTimeMillis();
        answer.setAnsweredAt(currentMillis);
        answer.setEditedAt(currentMillis);
        answer.setIsEdited("false");
        answer.setNoOfComments(0);
        answer.setNoOfVotes(0);
        answerRepository.save(answer);


        // updating the user answer rel db
        log.info("updating the user answer rel db");

        UserAnswerRel userAnswerRel = new UserAnswerRel();
        userAnswerRel.setRelId(UUID.randomUUID().toString());
        userAnswerRel.setUserId(answerDTO.getUserId());
        userAnswerRel.setAnswerId(answerId);
        userAnswerRel.setIsAnswered("true");
        userAnswerRel.setIsVoted("false");
        userAnswerRel.setIsCommented("false");

        userAnswerRelRepository.save(userAnswerRel);

        return answerId;
    }


    public List<VAnswer> getAnswersOfQuestion(String questionId){
        return vAnswerRepository.findByQuestionIdOrderByNoOfVotesDesc(questionId);
    }

    public Optional<Answer> getAnswerByAnswerId(String answerId) {
        return answerRepository.findById(answerId);
    }

    public void updateAnswer(Answer answer) {
        answerRepository.save(answer);
    }

    public void updateUserAnswerVoteRel(UserAnswerRel userAnswerRel) {
        userAnswerRelRepository.save(userAnswerRel);
    }

    public Optional<UserAnswerRel> getUserAnswerRel(UserAnswerRelDTO userAnswerRelDTO){
        return userAnswerRelRepository.findUserAnswerRel(userAnswerRelDTO.getUserId(),userAnswerRelDTO.getAnswerId());
    }

}
