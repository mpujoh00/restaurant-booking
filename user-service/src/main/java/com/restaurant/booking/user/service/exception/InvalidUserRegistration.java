package com.restaurant.booking.user.service.exception;

public class InvalidUserRegistration extends RuntimeException {

    public InvalidUserRegistration(String email) {
        super("Can't register a user " + email + " with role admin");
    }
}
