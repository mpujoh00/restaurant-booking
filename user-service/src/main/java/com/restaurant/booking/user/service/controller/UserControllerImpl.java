package com.restaurant.booking.user.service.controller;

import com.restaurant.booking.user.model.User;
import com.restaurant.booking.user.service.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
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
        log.info("Getting user with email " + email);
        return ResponseEntity.ok(userService.findByEmail(email));
    }
}
