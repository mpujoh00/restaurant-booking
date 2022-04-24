package com.restaurant.booking.user.service.controller;

import com.restaurant.booking.user.model.LoginRequest;
import com.restaurant.booking.user.model.LoginResponse;
import com.restaurant.booking.user.model.RegistrationRequest;
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
public class AuthenticationControllerImpl implements AuthenticationController{

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthenticationControllerImpl(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest){
        log.info("Logging in");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //String jwtToken = jwtUtils.generateJwtToken(authentication);

        User loggedUser = userService.findByEmail(loginRequest.getEmail());

//        return ResponseEntity.ok(new LoginResponse(jwtToken, loggedUser));
        return ResponseEntity.ok(new LoginResponse("token", loggedUser));
    }

    @Override
    public ResponseEntity<User> register(RegistrationRequest registrationRequest){
        return new ResponseEntity<>(userService.register(registrationRequest), HttpStatus.CREATED);
    }
}
