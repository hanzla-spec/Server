package app.auth.controller;

import app.auth.entity.Profile;
import app.auth.entity.User;
import app.auth.model.ProfileDTO;
import app.auth.model.ResponseDetails;
import app.auth.model.UserDTO;
import app.auth.security.*;
import app.auth.model.LoginDetails;
import app.auth.service.AuthService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("api/v1/auth")
@Slf4j
@Api(tags = "Authentication Management")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.token.validity}")
    private Long tokenValidity;

    @Autowired
    private AuthService authService;

    @PostMapping("register")
    @ApiOperation(value = "Registers a user")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO){

        log.info("Performing operation to register a user");

        ResponseDetails responseDetails = new ResponseDetails();

        if(userDTO == null || userDTO.getPassword()==null || userDTO.getUsername() ==null ||
        userDTO.getPassword().strip().equals("") || userDTO.getUsername().strip().equals("")){

            responseDetails.setMessage_code(-1);
            responseDetails.setMessage("Null user details");
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
        else {

            String emailRegex = "^(.+)@(\\S+)$";
            Pattern p = Pattern.compile(emailRegex);
            Matcher m = p.matcher(userDTO.getUsername());
            if(!m.find()){
                responseDetails.setMessage_code(-1);
                responseDetails.setMessage("Invalid email address");
                return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }

            Optional<User> userDB = authService.getUserByUsername(userDTO.getUsername());
            if (userDB.isPresent()) {
                responseDetails.setMessage_code(-1);
                responseDetails.setMessage("User already exists");
                return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
            } else {
                String userId = authService.registerUser(userDTO);
                log.info("User Registration done");
                responseDetails.setMessage_code(1);
                responseDetails.setMessage(userId);
            }
        }
        log.info("registration successful");
        return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.OK);
    }


    @PostMapping("login")
    @ApiOperation(value = "Logins User By providing access token")
    public ResponseEntity<?> login(@RequestBody LoginDetails loginDetails){

        log.info("Performing operation to login a user");

        ResponseDetails responseDetails = new ResponseDetails();
        JwtResponse response = new JwtResponse();

        if(loginDetails == null){
            responseDetails.setMessage_code(-1);
            responseDetails.setMessage("Null login details");
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
        else{
            Optional<User> userDB =  authService.loginUser(loginDetails);
            if(userDB.isPresent()){
                User user = userDB.get();
                if(!user.getPassword().equals(loginDetails.getPassword())){
                    responseDetails.setMessage_code(-1);
                    responseDetails.setMessage("Password didn't match");
                    return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
                }else {
                    String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
                    String refresh_token = jwtUtil.generateRefreshToken(user.getUsername(),user.getRole());
                    response.setType("Bearer");
                    response.setAccess_token(token);
                    response.setRefresh_token(refresh_token);
                    response.setExpiresInMillis(tokenValidity);
                    response.setUserId(user.getId());
                }
            }else{
                responseDetails.setMessage_code(-1);
                responseDetails.setMessage("User is not registered");
                return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }

        log.info("logged in successfully");
        return  new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("refresh-token")
    @ApiOperation("Refreshing the token using refresh token")
    public ResponseEntity<?> refreshToken(HttpServletRequest httpRequest){

        log.info("Performing operation to refresh the token");

        ResponseDetails responseDetails = new ResponseDetails();

        String refresh_token = httpRequest.getHeader("ReAuthorization");

        if(refresh_token!=null && refresh_token.startsWith("Bearer")){

            refresh_token = refresh_token.substring(7);

            try{
                jwtUtil.validateToken(refresh_token);
            }catch (JwtTokenExpiredException ex){
                responseDetails.setMessage_code(-1);
                responseDetails.setMessage("Refresh Token is expired");
                return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }catch (JwtTokenMissingException | JwtTokenMalformedException ex){
                responseDetails.setMessage_code(-1);
                responseDetails.setMessage("Invalid Refresh Token");
                return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }

            Claims claims = jwtUtil.getClaims(refresh_token);
            String username = String.valueOf(claims.get("username"));
            Optional<User> userDB =  authService.getUserByUsername(username);
            if(userDB.isPresent()){
                User user = userDB.get();
                String new_access_token = jwtUtil.generateToken(user.getUsername(), user.getRole());
                responseDetails.setMessage_code(1);
                responseDetails.setMessage(new_access_token);
            }else{
                responseDetails.setMessage_code(-1);
                responseDetails.setMessage("Refresh token doesn't validate the user");

                return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }

        }else{
            responseDetails.setMessage_code(-1);
            responseDetails.setMessage("Invalid/missing refresh token");

            log.info(refresh_token);
            return new ResponseEntity<>(responseDetails, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
        log.info("new token generated successfully");
        return new ResponseEntity<>(responseDetails,new HttpHeaders(),HttpStatus.OK);
    }

    @GetMapping(value = "profile/{userId}")
    @ApiOperation(value = "Getting profile from userId")
    public ResponseEntity<?> getProfileFromUsername(@PathVariable(value = "userId") String userId){

        log.info("Performing operation to fetch profile from userId");
        ResponseDetails responseDetails = new ResponseDetails();
        ProfileDTO profileDTO = new ProfileDTO();

        Optional<Profile> profileDB = authService.getProfileFromUserId(userId);
        if(!profileDB.isPresent()){
            responseDetails.setMessage_code(-1);
            responseDetails.setMessage("No profile found with this userId "+ userId);
            return  new ResponseEntity<>(responseDetails, new HttpHeaders() , HttpStatus.FORBIDDEN);
        }else{
            Profile profile = profileDB.get();
            ModelMapper mapper = new ModelMapper();
            profileDTO = mapper.map(profile, new TypeToken<ProfileDTO>(){}.getType());
            log.info("profile fetched successfully");
        }

        return new ResponseEntity<>(profileDTO, new HttpHeaders(), HttpStatus.OK);
    }

}
