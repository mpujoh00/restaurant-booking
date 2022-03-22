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

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
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
    public ResponseEntity<List<User>> getAllUsers(){
        log.info("Getting all users");
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @Override
    public ResponseEntity<User> getUserByEmail(String email){
        log.info("Getting user with email {}", email);
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @Override
    public ResponseEntity<User> updateUser(UpdateRequest updateRequest) {
        // TODO check if it is the current user

        log.info("Updating user with email {}", updateRequest.getEmail());

        // gets current user (unmodified)
        User user = userService.findByEmail(updateRequest.getEmail());

        // modifies user
        if (updateRequest.getFullname() != null)
            user.setFullname(updateRequest.getFullname());
        if (updateRequest.getPassword() != null)
            user.setPassword(updateRequest.getPassword());

        // saves user to database and returns it
        return ResponseEntity.ok(userService.update(user));
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
            // changes password
            User user = userService.findByEmail(userEmail);
            user.setPassword(updatePasswordRequest.getNewPassword());
            // saves user
            userService.save(user);

            // sets the user as logged with the new password
            Authentication authenticationNewPass = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userEmail, updatePasswordRequest.getNewPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticationNewPass);
            String jwtToken = jwtUtils.generateJwtToken(authenticationNewPass);
            return ResponseEntity.ok(jwtToken);
        }
        catch (Exception e){
            log.error("Password is incorrect");
            return new ResponseEntity("Incorrect password", HttpStatus.BAD_REQUEST);
        }
    }
    // TODO cambiar todos los stings de + a {}
}
