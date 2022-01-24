package com.restaurant.booking.user.service.controller;

import com.restaurant.booking.user.model.User;
import com.restaurant.booking.user.service.service.UserServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/login")
public class AuthenticationController {

    private UserServiceImpl userService;

    @Autowired
    public AuthenticationController(UserServiceImpl userService){
        this.userService = userService;
    }

    @GetMapping
    public String welcomeLogin(){
        return "Welcome to login!";
    }

    @PostMapping
    public ResponseEntity<User> login(){
        log.info("Logging in");
        return null;
    }
}
