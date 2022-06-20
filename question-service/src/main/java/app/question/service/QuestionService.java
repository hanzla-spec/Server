package app.question.service;

import app.question.entity.Question;
import app.question.entity.Tags;
import app.question.entity.UserQuestionRel;
import app.question.entity.VQuestion;
import app.question.model.QuestionDTO;
import app.question.model.UserQuestionRelDTO;
import app.question.repository.QuestionRepository;
import app.question.repository.TagRepository;
import app.question.repository.UserQuestionRelRepository;
import app.question.repository.VQuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@ComponentScan
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private VQuestionRepository vQuestionRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserQuestionRelRepository userQuestionRelRepository;


    public String postQuestion(QuestionDTO questionDTO){

        Question question = new Question();
        String questionId = UUID.randomUUID().toString();
        question.setQuestionId(questionId);
        question.setUserId(questionDTO.getUserId());
        question.setTitle(questionDTO.getTitle());
        question.setBody(questionDTO.getBody());
        question.setTags(questionDTO.getTags());
        question.setNoOfVotes(0); // by default votes are zero
        question.setNoOfAnswers(0); //default zero
        question.setNoOfViews(0);//default zero
        Long currentTimeMillis = System.currentTimeMillis(); // by default current date is set
        question.setAskedAt(currentTimeMillis);
        question.setIsEdited("false");
        question.setEditedAt(currentTimeMillis);
        questionRepository.save(question);


        // adding stats to user-question-rel db
        log.info("adding stats to user-question-rel db");

        UserQuestionRel userQuestionRel = new UserQuestionRel();
        userQuestionRel.setRelId(UUID.randomUUID().toString());
        userQuestionRel.setQuestionId(questionId);
        userQuestionRel.setUserId(questionDTO.getUserId());
        userQuestionRel.setIsAsked("true");
        userQuestionRel.setIsVoted("false");//defaults
        userQuestionRelRepository.save(userQuestionRel);

        return questionId;
    }

    public List<VQuestion> getAllVQuestions(){
        return vQuestionRepository.findAll();
    }

    public Optional<Tags> getTags(){
        return tagRepository.findById("12345");
    }

    public void postTag(Tags tag){
        tagRepository.save(tag);
    }

    public void updateQuestion(Question question){
        questionRepository.save(question);
    }

    public Optional<Question> getQuestionByQuestionId(String questionId){
        return questionRepository.findById(questionId);
    }

    public Optional<VQuestion> getVQuestionByQuestionId(String questionId){
        return vQuestionRepository.findById(questionId);
    }

    public Optional<UserQuestionRel> getUserQuestionVoteRel(UserQuestionRelDTO userQuestionRelDTO) {
        return userQuestionRelRepository.findUserQuestionVoteRel(userQuestionRelDTO.getUserId(),
                                                           userQuestionRelDTO.getQuestionId());
    }

    public void updateUserQuestionRel(UserQuestionRel userQuestionRel) {
        userQuestionRelRepository.save(userQuestionRel);
    }
}
