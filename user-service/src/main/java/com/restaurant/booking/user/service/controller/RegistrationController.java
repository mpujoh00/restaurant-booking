package com.restaurant.booking.user.service.controller;

import com.restaurant.booking.user.model.RegistrationRequest;
import com.restaurant.booking.user.model.User;
import com.restaurant.booking.user.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService){
        this.userService = userService;
    }

    @PutMapping
    public ResponseEntity<User> register(@RequestBody @Valid RegistrationRequest request){
        return new ResponseEntity<>(userService.register(request), HttpStatus.CREATED);
    }

}
