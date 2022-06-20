package app.question.controller;

import app.question.entity.Answer;
import app.question.entity.Question;
import app.question.entity.UserAnswerRel;
import app.question.entity.UserQuestionRel;
import app.question.model.*;
import app.question.service.AnswerService;
import app.question.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "private/v1/answer")
@Api(tags = "Answer management")
@Slf4j
public class PrivateAnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;


    @PostMapping(value = "/post")
    @ApiOperation(value = "Post answer for question")
    public ResponseEntity<?> postAnswerForQuestion(@RequestHeader(value = "username", required = true)String h_username,
                                                   @RequestHeader(value = "role", required = true) String h_role,
                                                   @RequestBody AnswerDTO answerDTO){

        log.info("Performing operation for posting an answer to question");
        ResponseDetails responseDetails = new ResponseDetails();

        if(answerDTO == null || answerDTO.getBody()==null){
            responseDetails.setMessage_code(-1);
            responseDetails.setMessage("Null value not allowed");
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }else{
            String answerId = answerService.postAnswer(answerDTO);

            Optional<Question> questionDB = questionService.getQuestionByQuestionId(answerDTO.getQuestionId());
            if(!questionDB.isPresent()){
                responseDetails.setMessage_code(-1);
                responseDetails.setMessage("No question present with the question Id  "+ answerDTO.getQuestionId());
                return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }else{
                Question question = questionDB.get();
                question.setNoOfAnswers(question.getNoOfAnswers() + 1);
                log.info("answers count increased for the questionId "+ question.getQuestionId());
                questionService.updateQuestion(question);
            }

            log.info("answer posted successfully");
            responseDetails.setMessage_code(1);
            responseDetails.setMessage(answerId);
        }
        return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.OK);
    }

    @ApiOperation(value = "To increase no of vote")
    @PutMapping(value = "/upvote")
    private ResponseEntity<?> increaseVoteOfQuestion(@RequestHeader(value = "username", required = true)String h_username,
                                                     @RequestHeader(value = "role", required = true) String h_role,
                                                     @RequestBody UserAnswerRelDTO userAnswerRelDTO){

        log.info("Performing operation to increase the count of vote");
        ResponseDetails responseDetails = new ResponseDetails();

        if(userAnswerRelDTO == null || userAnswerRelDTO.getAnswerId().trim().equals("") ||
                userAnswerRelDTO.getUserId().trim().equals("")){
            responseDetails.setMessage("Null values not accepted");
            responseDetails.setMessage_code(-1);
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }else{
            Optional<Answer> answerDB = answerService.getAnswerByAnswerId(userAnswerRelDTO.getAnswerId());
            if(!answerDB.isPresent()){
                responseDetails.setMessage_code(-1);
                responseDetails.setMessage("No answer present with the answer Id  "+ userAnswerRelDTO.getAnswerId());
                return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }else{
                log.info("performing operation to update user question rel");

                Optional<UserAnswerRel> userAnswerRelDB = answerService.getUserAnswerRel(userAnswerRelDTO);
                if(!userAnswerRelDB.isPresent() || userAnswerRelDB.get().getIsVoted().equals("false")){

                    if(!userAnswerRelDB.isPresent()){
                        userAnswerRelDTO.setRelId(UUID.randomUUID().toString());
                        userAnswerRelDTO.setIsVoted("true");
                        ModelMapper mapper = new ModelMapper();
                        UserAnswerRel userAnswerRel = mapper.map(userAnswerRelDTO, new TypeToken<UserAnswerRel>(){}.getType());
                        answerService.updateUserAnswerVoteRel(userAnswerRel);
                    }else{
                        UserAnswerRel userAnswerRel = userAnswerRelDB.get();
                        userAnswerRel.setIsVoted("true");
                        answerService.updateUserAnswerVoteRel(userAnswerRel);
                    }

                    log.info("updated user answer rel successfully");

                    Answer answer = answerDB.get();
                    answer.setNoOfVotes(answer.getNoOfVotes()+1);
                    responseDetails.setMessage("vote count increased successfully");
                    responseDetails.setMessage_code(1);
                    answerService.updateAnswer(answer);


                }else{
                    responseDetails.setMessage_code(-1);
                    responseDetails.setMessage("you already voted the answer");
                    return new ResponseEntity<>(responseDetails, new HttpHeaders() , HttpStatus.FORBIDDEN);
                }

            }
        }
        log.info("vote count increased successfully");

        return new ResponseEntity<>(responseDetails ,new HttpHeaders(), HttpStatus.OK);
    }



    @PutMapping(value = "/edit")
    @ApiOperation(value = "Edit answer for question")
    public ResponseEntity<?> editAnswerForQuestion(@RequestHeader(value = "username", required = true)String h_username,
                                                   @RequestHeader(value = "role", required = true) String h_role,
                                                   @RequestBody AnswerDTO answerDTO){

        log.info("Performing operation for editing an answer to question");
        ResponseDetails responseDetails = new ResponseDetails();

        if(answerDTO == null || answerDTO.getBody()==null){
            responseDetails.setMessage_code(-1);
            responseDetails.setMessage("Null value not allowed");
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }else{

            Optional<Answer> answerDB = answerService.getAnswerByAnswerId(answerDTO.getAnswerId());
            if(!answerDB.isPresent()){
                responseDetails.setMessage_code(-1);
                responseDetails.setMessage("No answer present with the answer Id  "+ answerDTO.getAnswerId());
                return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }else {
                answerDTO.setIsEdited("true");
                answerDTO.setEditedAt(System.currentTimeMillis());
                Answer answer = new ModelMapper().map(answerDTO, new TypeToken<Answer>(){}.getType());
                answerService.updateAnswer(answer);
                responseDetails.setMessage_code(1);
                responseDetails.setMessage(answerDTO.getAnswerId());
            }
        }
        log.info("answer edited successfully");
        return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.OK);
    }

}
