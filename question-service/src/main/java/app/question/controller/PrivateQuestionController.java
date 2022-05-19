package app.question.controller;

import app.question.model.QuestionDTO;
import app.question.model.ResponseDetails;
import app.question.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
