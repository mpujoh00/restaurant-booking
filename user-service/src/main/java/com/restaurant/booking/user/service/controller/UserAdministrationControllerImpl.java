package com.restaurant.booking.user.service.controller;

import com.restaurant.booking.user.model.RegistrationRequest;
import com.restaurant.booking.user.model.User;
import com.restaurant.booking.user.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
public class UserAdministrationControllerImpl implements UserAdministrationController {

    private final UserService userService;

    @Autowired
    public UserAdministrationControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<List<User>> getAllUsers(){

        return ResponseEntity.ok(userService.findAllUsers());
    }

    @Override
    public ResponseEntity<User> registerAdmin(RegistrationRequest registrationRequest) {

        return new ResponseEntity<>(userService.registerAdminUser(registrationRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteAdmin(String email) {

        userService.deleteAdmin(userService.findByEmail(email));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<User> updateUserStatus(String email) {

        return ResponseEntity.ok(userService.changeUserStatus(userService.findByEmail(email)));
    }
}
