package app.question.controller;

import app.question.entity.VAnswer;
import app.question.model.ResponseDetails;
import app.question.service.AnswerService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/answer")
@Api(tags = "Answer management")
@Slf4j
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @GetMapping(value = "/{questionId}")
    public ResponseEntity<?> getAnswersOfQuestion(@PathVariable(value = "questionId") String questionId){

        log.info("Performing operation to get All answers of a question");
        ResponseDetails responseDetails = new ResponseDetails();

        List<VAnswer> answerList = answerService.getAnswersOfQuestion(questionId);
        if(answerList.isEmpty()){
            responseDetails.setMessage_code(-1);
            responseDetails.setMessage("NO ANSWERS FOUND");
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(answerList, new HttpHeaders(), HttpStatus.OK);
    }

}
