package app.question.controller;

import app.question.entity.VComment;
import app.question.model.ResponseDetails;
import app.question.model.VCommentDTO;
import app.question.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
@Api(tags = "Comment Management")
@RequestMapping(value = "api/v1/comment")
@Slf4j
public class CommentController {

    @Autowired
    private CommentService commentService;


    @ApiOperation(value = "Get comments of a answer")
    @GetMapping(value = "/get/{answerId}")
    public ResponseEntity<?> getCommentsOfAnswer(@PathVariable(value = "answerId") String answerId){

        log.info("Performing operation to get comments of answer");
        ResponseDetails responseDetails = new ResponseDetails();

        List<VComment> vCommentList = commentService.getVCommentsOfAnswer(answerId);
        if(vCommentList.isEmpty()){
            responseDetails.setMessage_code(-1);
            responseDetails.setMessage("No comments found");
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }

        ModelMapper mapper = new ModelMapper();
        List<VCommentDTO> vCommentDTOList = mapper.map(vCommentList, new TypeToken<List<VCommentDTO>>(){}.getType());

        log.info("comments fetched successfully");

        return new ResponseEntity<>(vCommentDTOList, new HttpHeaders(), HttpStatus.OK);
    }
}
