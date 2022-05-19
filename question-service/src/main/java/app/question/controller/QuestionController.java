package app.question.controller;

import app.question.entity.Question;
import app.question.entity.VQuestion;
import app.question.model.QuestionDTO;
import app.question.model.ResponseDetails;
import app.question.model.VQuestionDTO;
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

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/question")
@Slf4j
@Api(tags = "Questions Management")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping(value = "/all")
    @ApiOperation(value = "Get all questions")
    public ResponseEntity<?> getAllVQuestion(){

        log.info("performing operation to get all the questions");
        ResponseDetails responseDetails = new ResponseDetails();

        List<VQuestion> questionList = questionService.getAllVQuestions();
        if(questionList==null || questionList.isEmpty()){
            responseDetails.setMessage_code(-1);
            responseDetails.setMessage("No questions found");
            return new ResponseEntity<>(responseDetails, new HttpHeaders() , HttpStatus.FORBIDDEN);
        }

        ModelMapper mapper = new ModelMapper();
        List<VQuestionDTO> vQuestionDTOS = mapper.map(questionList, new TypeToken<List<VQuestionDTO>>(){}.getType());

        log.info("v questions retrieved successfully");
        return new ResponseEntity<>(vQuestionDTOS, new HttpHeaders() , HttpStatus.OK);
    }

}
