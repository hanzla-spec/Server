package app.auth.controller;

import app.auth.entity.User;
import app.auth.model.ResponseDetails;
import app.auth.service.AuthService;
import app.auth.service.EmailService;
import app.auth.service.EmailTemplate;
import app.auth.service.OTPService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("private/v1/email")
@Slf4j
@Api(tags = "Email Authentication Management")
public class EmailController {

    @Autowired
    public OTPService otpService;

    @Autowired
    public EmailService emailService;


    @Autowired
    private AuthService authService;


    @PostMapping("generate-otp")
    @ApiOperation(value = "Get OTP verify email")
    public ResponseEntity<?> generateOTP(@RequestHeader(value = "username" ,required = true) String h_username,
                                         @RequestHeader(value = "role" , required = true) String h_role) throws MessagingException {

        log.info("Performing operation to generate otp");
        ResponseDetails responseDetails = new ResponseDetails();

        String OTP = otpService.generateOTP(h_username);
        EmailTemplate template = new EmailTemplate("SendOTP.html");
        Map<String,String> replacements = new HashMap();
        replacements.put("user", h_username);
        replacements.put("OTP", OTP);
        String message = template.getTemplate(replacements);
        responseDetails.setMessage_code(1);
        responseDetails.setMessage("OTP sent");
        try{
            emailService.sendOtpMessage(h_username, "findAll OTP verification", message);
        }catch (MessagingException ex){
            log.info("Exception occurred during OTP mail sending");
            throw new MessagingException();
        }
        return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.OK);
    }



    @PostMapping(value ="validate-otp/{OTP}")
    @ApiOperation(value = "Validate OTP")
    public ResponseEntity<?> validateOtp(@RequestHeader(value = "username" ,required = true) String h_username,
                                                @RequestHeader(value = "role" , required = true) String h_role,
                                                @PathVariable("OTP") String OTP){

        ResponseDetails responseDetails = new ResponseDetails();
        final String SUCCESS = "Email Verified Successfully";
        final String FAIL = "Invalid or expired OTP";
        boolean verified = false;

        if(OTP != null && !OTP.equals("")){
            String serverOtp = otpService.getOtp(h_username);
            log.info(serverOtp);
            if(serverOtp != null){
                if(OTP.equals(serverOtp)){
                    otpService.clearOTP(h_username);
                    verified = true;
                }
                else {
                    verified = false;
                }
            }else {
                verified = false;
            }
        }else {
            verified = false;
        }

        if(verified){
            Optional<User> userDB = authService.getUserByUsername(h_username);
            if(!userDB.isEmpty()){
                User user = userDB.get();
                user.setIsVerified("true");
                authService.updateUser(user);
                log.info("user verification updated");
                responseDetails.setMessage(SUCCESS);
                responseDetails.setMessage_code(1);
            }else{
                responseDetails.setMessage_code(-1);
                responseDetails.setMessage("User not found");
                return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            responseDetails.setMessage(FAIL);
            responseDetails.setMessage_code(-1);
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(responseDetails,new HttpHeaders(), HttpStatus.OK);
    }

}
