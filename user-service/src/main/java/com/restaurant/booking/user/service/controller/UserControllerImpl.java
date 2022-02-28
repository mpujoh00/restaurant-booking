package com.restaurant.booking.user.service.controller;

import com.restaurant.booking.user.model.UpdatePasswordRequest;
import com.restaurant.booking.user.model.UpdateRequest;
import com.restaurant.booking.user.model.User;
import com.restaurant.booking.user.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserControllerImpl implements UserController{

    private final UserService userService;

    @Autowired
    public UserControllerImpl(UserService userService) {
        this.userService = userService;
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
    public ResponseEntity<User> updateUserPassword(UpdatePasswordRequest updatePasswordRequest) {
        log.info("Updating password of user with email {}", updatePasswordRequest.getEmail());

        // gets current user
        User user = userService.findByEmail(updatePasswordRequest.getEmail());

        // checks if the current password is correct

        // changes to new password (encrypting)

        return null;
    }

    // TODO cambiar todos los stings de + a {}
}
