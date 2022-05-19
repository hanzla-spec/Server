package app.auth.controller;

import app.auth.entity.User;
import app.auth.model.ResponseDetails;
import app.auth.service.AuthService;
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
@RequestMapping("private/v1/auth")
@Slf4j
@Api(tags = "Authentication Management")
public class PrivateAuthController {

    @Autowired
    AuthService authService;

    @GetMapping("getId/{username}")
    @ApiOperation(value = "Get userId from username")
    public ResponseEntity<?> getIdByUsername(@RequestHeader(value = "username" ,required = true)String h_username
                                             ,@RequestHeader(value = "role" , required = true) String h_role,
                                             @PathVariable("username") String username){
        log.info("Performing operation for getting user id by username");
        ResponseDetails responseDetails = new ResponseDetails();

        Optional<User> userDB = authService.getUserByUsername(username);
        if(userDB.isPresent()){
            User user = userDB.get();
            responseDetails.setMessage(user.getId());
            responseDetails.setMessage_code(1);
        }else{
            responseDetails.setMessage_code(-1);
            responseDetails.setMessage("User doesn't exists");
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
        log.info("retrieved user_id successfully");
        return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.OK);
    }

}
