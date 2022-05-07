package com.restaurant.booking.user.service.exception;

public class UserLoginDisabledException extends RuntimeException{
    public UserLoginDisabledException(String userEmail) {
        super("Can't login, user " + userEmail + " is disabled");
    }
}
