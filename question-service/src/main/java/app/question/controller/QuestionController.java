package app.question.controller;

import app.question.entity.Question;
import app.question.entity.Tags;
import app.question.entity.VQuestion;
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
import java.util.Optional;

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

    @GetMapping(value = "/allTags")
    @ApiOperation(value = "Get all tags")
    private ResponseEntity<?> getAllTags(){
        log.info("Performing operation to get All tags");
        ResponseDetails responseDetails = new ResponseDetails();
        Tags tags = null;
        Optional<Tags> tagsDB = questionService.getTags();
        if(!tagsDB.isPresent()){
            responseDetails.setMessage_code(-1);
            responseDetails.setMessage("No tags present");
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
        tags = tagsDB.get();

        return new ResponseEntity<>(tags, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(value = "/get/{questionId}")
    @ApiOperation(value = "Get a question for question ID")
    private ResponseEntity<?> getVQuestionForQuestionId(@PathVariable(value = "questionId") String questionId){
        log.info("Performing operation to get a question by question id");
        ResponseDetails responseDetails = new ResponseDetails();
        VQuestion question = null;
        if(questionId == null || questionId.trim().equals("")){
            responseDetails.setMessage("Null values not accepted");
            responseDetails.setMessage_code(-1);
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }else{

            Optional<VQuestion> vQuestionDB = questionService.getVQuestionByQuestionId(questionId);
            if(!vQuestionDB.isPresent()){
                responseDetails.setMessage_code(-1);
                responseDetails.setMessage("No question present with the question Id  "+ questionId);
                return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }else{
                question = vQuestionDB.get();
            }
        }

        log.info("question fetched successfully");

        return new ResponseEntity<>(question ,new HttpHeaders(), HttpStatus.OK);
    }

    @ApiOperation(value = "To increase no of views")
    @PutMapping(value = "/upView/{questionId}")
    private ResponseEntity<?> increaseViewOfQuestion(@PathVariable(value = "questionId") String questionId){

        log.info("Performing operation to increase the count of view");
        ResponseDetails responseDetails = new ResponseDetails();

        if(questionId == null || questionId.trim().equals("")){
            responseDetails.setMessage("Null values not accepted");
            responseDetails.setMessage_code(-1);
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }else{
            Optional<Question> questionDB = questionService.getQuestionByQuestionId(questionId);
            if(!questionDB.isPresent()){
                responseDetails.setMessage_code(-1);
                responseDetails.setMessage("No question present with the question Id  "+ questionId);
                return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }else{
                Question question = questionDB.get();
                question.setNoOfViews(question.getNoOfViews()+1);
                responseDetails.setMessage("view count increased successfully");
                responseDetails.setMessage_code(1);
                questionService.updateQuestion(question);
            }
        }
        log.info("view count increased successfully");

        return new ResponseEntity<>(responseDetails ,new HttpHeaders(), HttpStatus.OK);
    }

}
