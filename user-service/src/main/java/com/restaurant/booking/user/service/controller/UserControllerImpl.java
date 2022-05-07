package com.restaurant.booking.user.service.controller;

import com.restaurant.booking.user.model.UpdatePasswordRequest;
import com.restaurant.booking.user.model.UpdateRequest;
import com.restaurant.booking.user.model.User;
import com.restaurant.booking.user.service.security.JwtUtils;
import com.restaurant.booking.user.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
public class UserControllerImpl implements UserController{

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserControllerImpl(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ResponseEntity<User> getUserByEmail(String email){
        log.info("Getting user with email {}", email);
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @Override
    public ResponseEntity<User> updateUser(UpdateRequest updateRequest) {
        log.info("Updating user with email {}", updateRequest.getEmail());

        // checks if it is the current user
        if(isNotCurrentUser(updateRequest.getEmail())){
            // can't update another user
            log.error("Can't update another user diferent than self");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        // saves user to database and returns it
        return ResponseEntity.ok(userService.update(userService.findByEmail(updateRequest.getEmail()), updateRequest));
    }

    @Override
    public ResponseEntity<String> updateUserPassword(UpdatePasswordRequest updatePasswordRequest) {

        // gets current user
        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Updating password of user with email {}", userEmail);

        // checks if the current password is correct
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userEmail, updatePasswordRequest.getCurrentPassword()));
            // sets the user as logged with the new password
            Authentication authenticationNewPass = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userEmail, updatePasswordRequest.getNewPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticationNewPass);
            //String jwtToken = jwtUtils.generateJwtToken(authenticationNewPass);
//            return ResponseEntity.ok(jwtToken);
            return ResponseEntity.ok("token");
        }
        catch (Exception e){
            log.error("Password is incorrect");
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Void> deleteUser(String email) {
        log.info("Deleting user with email {}", email);

        // checks if it is the current user
        if(isNotCurrentUser(email)){
            // can't update another user
            log.error("Can't delete another user different than self");
            return ResponseEntity.badRequest().build();
        }

        userService.delete(userService.findByEmail(email));
        return ResponseEntity.ok().build();
    }

    private boolean isNotCurrentUser(String emailRequest){
        //String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //return !userEmail.equals(emailRequest);
        return false;
    }
}
