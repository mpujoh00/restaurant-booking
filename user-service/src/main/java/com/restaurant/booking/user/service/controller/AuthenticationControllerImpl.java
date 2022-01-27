package com.restaurant.booking.user.service.controller;

import com.restaurant.booking.user.model.AuthenticationToken;
import com.restaurant.booking.user.model.LoginRequest;
import com.restaurant.booking.user.model.RegistrationRequest;
import com.restaurant.booking.user.model.User;
import com.restaurant.booking.user.service.security.JwtUtils;
import com.restaurant.booking.user.service.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
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
    public ResponseEntity<AuthenticationToken> login(LoginRequest loginRequest){
        log.info("Logging in");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok(new AuthenticationToken(jwtToken));
    }

    @Override
    public ResponseEntity<User> register(RegistrationRequest request){
        return new ResponseEntity<>(userService.register(request), HttpStatus.CREATED);
    }
}
