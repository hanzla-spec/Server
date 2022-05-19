package app.auth.service;

import app.auth.entity.Profile;
import app.auth.entity.User;
import app.auth.model.LoginDetails;
import app.auth.model.ProfileDTO;
import app.auth.model.ResponseDetails;
import app.auth.model.UserDTO;
import app.auth.repository.AuthRepository;
import app.auth.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private ProfileRepository profileRepository;

    public String registerUser(UserDTO userDTO){
        User user = new User();
        String userId = UUID.randomUUID().toString();
        user.setId(userId);
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setRole("USER"); // by default all users has a role of USER
        authRepository.save(user);

        log.info("user is registered");

        log.info("Performing Operation to set Profile by default");

        Profile profile = new Profile();
        profile.setProfileId(UUID.randomUUID().toString());
        profile.setUserId(userId);
        profile.setUsername(userDTO.getUsername());
        profile.setDisplayName(userDTO.getUsername().split("@")[0]); // same as email first part
        profile.setPoints(0L); //default point 0
        profile.setAvatar("default"); // default avatar.
        profile.setBadge("default");

        profileRepository.save(profile);

        log.info("Default Profile set");
        return userId;
    }

    public Optional<User> loginUser(LoginDetails loginDetails){
        Optional<User> userDB =  authRepository.findByUsername(loginDetails.getUsername());
        return userDB;
    }

    public Optional<User> getUserByUsername(String username){
        return authRepository.findByUsername(username);
    }

    public Optional<Profile> getProfileFromUserId(String userId){
        return profileRepository.findProfileFromUserId(userId);
    }

}
