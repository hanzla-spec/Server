package app.question.controller;

import app.question.entity.Answer;
import app.question.model.CommentDTO;
import app.question.model.ResponseDetails;
import app.question.service.AnswerService;
import app.question.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Api(tags = "Comment Management")
@RequestMapping(value = "private/v1/comment")
@Slf4j
public class PrivateCommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private AnswerService answerService;

    @ApiOperation(value = "To add comment in answer")
    @PostMapping(value = "/post")
    private ResponseEntity<?> postCommentForAnswer(@RequestHeader(value = "username", required = true)String h_username,
                                                   @RequestHeader(value = "role", required = true) String h_role,
                                                   @RequestBody CommentDTO commentDTO){

        log.info("Performing operation to add comment to the answer");
        ResponseDetails responseDetails = new ResponseDetails();

        if(commentDTO == null || commentDTO.getAnswerId().trim().equals("") ||
                                           commentDTO.getUserId().trim().equals("")){
            responseDetails.setMessage("Null values not accepted");
            responseDetails.setMessage_code(-1);
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }else{
            Optional<Answer> answerDB = answerService.getAnswerByAnswerId(commentDTO.getAnswerId());
            if(!answerDB.isPresent()){
                responseDetails.setMessage_code(-1);
                responseDetails.setMessage("no answer present with this answer id");
                return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }


            commentService.postComment(commentDTO);
            log.info("comment posted successfully");


            log.info("performing operation to increase no of answers");
            Answer answer = answerDB.get();
            answer.setNoOfComments(answer.getNoOfComments() + 1);
            answerService.updateAnswer(answer);

            responseDetails.setMessage_code(1);
            responseDetails.setMessage("comment posted successfully");

        }


        return new ResponseEntity<>(responseDetails ,new HttpHeaders(), HttpStatus.OK);
    }
}
