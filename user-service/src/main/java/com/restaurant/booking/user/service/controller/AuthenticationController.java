package com.restaurant.booking.user.service.controller;

import com.restaurant.booking.user.model.LoginRequest;
import com.restaurant.booking.user.model.RegistrationRequest;
import com.restaurant.booking.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Tag(name = "Authentication")
@RequestMapping("/auth")
public interface AuthenticationController {

    @Operation(description = "Logs user and returns authentication token", operationId = "login")
    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest);

    @Operation(description = "Registers and returns user", operationId = "register")
    @PostMapping("/register")
    ResponseEntity<User> register(@RequestBody @Valid RegistrationRequest registrationRequest);
}
