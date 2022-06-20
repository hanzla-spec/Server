package app.question.controller;

import app.question.entity.Question;
import app.question.entity.Tags;
import app.question.entity.UserQuestionRel;
import app.question.model.QuestionDTO;
import app.question.model.ResponseDetails;
import app.question.model.UserQuestionRelDTO;
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
@RequestMapping(value = "private/v1/question")
@Slf4j
@Api(tags = "Questions Management")
public class PrivateQuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping(value = "/post")
    @ApiOperation(value = "To post a question")
    public ResponseEntity<?> postQuestion(@RequestHeader(value = "username", required = true)String h_username,
                                          @RequestHeader(value = "role", required = true) String h_role,
                                          @RequestBody(required = true) QuestionDTO questionDTO){

        log.info("Performing operation to post a question");
        ResponseDetails responseDetails = new ResponseDetails();

        if(questionDTO == null || questionDTO.getBody()==null || questionDTO.getTitle()==null ||questionDTO.getUserId()==null){
            responseDetails.setMessage_code(-1);
            responseDetails.setMessage("Null request body not accepted");
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }

        String questionId = questionService.postQuestion(questionDTO);
        responseDetails.setMessage_code(1);
        responseDetails.setMessage(questionId);

        log.info("Question posted successfully");

        return new ResponseEntity<>(responseDetails, new HttpHeaders() , HttpStatus.OK);
    }

    @PostMapping(value = "/tag/post")
    @ApiOperation(value = "To post tags")
    public ResponseEntity<?> postTags(@RequestHeader(value = "username", required = true)String h_username,
                                      @RequestHeader(value = "role", required = true) String h_role,
                                      @RequestBody Tags tags){
        log.info("Performing operation to add new tags");
        ResponseDetails responseDetails = new ResponseDetails();

        if(tags.getTag().trim().equals("") || tags.getTag() == null){
            responseDetails.setMessage_code(-1);
            responseDetails.setMessage("Empty tag not allowed");
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }else{
            Optional<Tags> tagsDB = questionService.getTags();
            if(!tagsDB.isPresent()){
                log.info("performing operation to add first tag");
                Tags firstTag = new Tags();
                firstTag.setTagId("12345");
                firstTag.setTag(tags.getTag());
                questionService.postTag(firstTag);
                responseDetails.setMessage_code(1);
                responseDetails.setMessage("Fist Tag  added");
                return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.OK);
            }else{
                Tags currentTag = tagsDB.get();
                // duplicate tag filtering is already done in ui
                currentTag.setTag(currentTag.getTag().trim() + " "+ tags.getTag().trim());
                questionService.postTag(currentTag);
                responseDetails.setMessage("tag added successfully");
                responseDetails.setMessage_code(1);
            }
        }

        return new ResponseEntity<>(responseDetails, new HttpHeaders() , HttpStatus.OK);
    }


    @ApiOperation(value = "To increase no of vote")
    @PutMapping(value = "/upvote")
    private ResponseEntity<?> increaseVoteOfQuestion(@RequestHeader(value = "username", required = true)String h_username,
                                                     @RequestHeader(value = "role", required = true) String h_role,
                                                     @RequestBody UserQuestionRelDTO userQuestionRelDTO){

        log.info("Performing operation to increase the count of vote");
        ResponseDetails responseDetails = new ResponseDetails();

        if(userQuestionRelDTO == null || userQuestionRelDTO.getQuestionId().trim().equals("") ||
                userQuestionRelDTO.getUserId().trim().equals("")){
            responseDetails.setMessage("Null values not accepted");
            responseDetails.setMessage_code(-1);
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }else{
            Optional<Question> questionDB = questionService.getQuestionByQuestionId(userQuestionRelDTO.getQuestionId());
            if(!questionDB.isPresent()){
                responseDetails.setMessage_code(-1);
                responseDetails.setMessage("No question present with the question Id  "+ userQuestionRelDTO.getQuestionId());
                return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }else{
                log.info("performing operation to update user question rel");

                Optional<UserQuestionRel> userQuestionRelDB =  questionService.getUserQuestionVoteRel(userQuestionRelDTO);
                if(!userQuestionRelDB.isPresent() || userQuestionRelDB.get().getIsVoted().equals("false")){
                    if(!userQuestionRelDB.isPresent()){
                        userQuestionRelDTO.setRelId(UUID.randomUUID().toString());
                        userQuestionRelDTO.setIsVoted("true");
                        ModelMapper mapper = new ModelMapper();
                        UserQuestionRel userQuestionRel = mapper.map(userQuestionRelDTO, new TypeToken<UserQuestionRel>(){}.getType());
                        questionService.updateUserQuestionRel(userQuestionRel);
                    }else{
                        UserQuestionRel userQuestionRel = userQuestionRelDB.get();
                        userQuestionRel.setIsVoted("true");
                        questionService.updateUserQuestionRel(userQuestionRel);
                    }

                    log.info("user question rel updated successfully");

                    Question question = questionDB.get();
                    question.setNoOfVotes(question.getNoOfVotes()+1);
                    responseDetails.setMessage("vote count increased successfully");
                    responseDetails.setMessage_code(1);
                    questionService.updateQuestion(question);



                }else{
                    responseDetails.setMessage_code(-1);
                    responseDetails.setMessage("you already voted the question");
                    return new ResponseEntity<>(responseDetails, new HttpHeaders() , HttpStatus.FORBIDDEN);
                }

                log.info("updated user question rel successfully");
            }
        }
        log.info("vote count increased successfully");

        return new ResponseEntity<>(responseDetails ,new HttpHeaders(), HttpStatus.OK);
    }


    @ApiOperation(value = "To edit question")
    @PutMapping(value = "/edit")
    public ResponseEntity<?> editQuestion(@RequestHeader(value = "username", required = true)String h_username,
                                          @RequestHeader(value = "role", required = true) String h_role,
                                          @RequestBody(required = true) QuestionDTO questionDTO){

        log.info("Performing operation to edit a question");
        ResponseDetails responseDetails = new ResponseDetails();

        if(questionDTO == null || questionDTO.getBody()==null || questionDTO.getTitle()==null ||questionDTO.getUserId()==null){
            responseDetails.setMessage_code(-1);
            responseDetails.setMessage("Null request body not accepted");
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }

        Optional<Question> questionDB = questionService.getQuestionByQuestionId(questionDTO.getQuestionId());
        if(!questionDB.isPresent()){
            responseDetails.setMessage("question is not present with this question id");
            responseDetails.setMessage_code(-1);
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }else{
            questionDTO.setEditedAt(System.currentTimeMillis());
            questionDTO.setIsEdited("true");
            ModelMapper mapper = new ModelMapper();
            Question question = mapper.map(questionDTO , new TypeToken<Question>(){}.getType());
            questionService.updateQuestion(question);
            responseDetails.setMessage_code(1);
            responseDetails.setMessage(questionDTO.getQuestionId());
        }

        log.info("Question edited successfully");

        return new ResponseEntity<>(responseDetails, new HttpHeaders() , HttpStatus.OK);
    }

}
