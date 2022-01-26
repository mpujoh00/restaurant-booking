package com.restaurant.booking.user.service.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/home")
public class TryController {

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String welcomeLogin(){
        return "Welcome to home!";
    }
}
