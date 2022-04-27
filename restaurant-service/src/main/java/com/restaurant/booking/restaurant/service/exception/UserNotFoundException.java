package com.restaurant.booking.restaurant.service.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String email){
        super("User with email: " + email + " not found");
    }
}