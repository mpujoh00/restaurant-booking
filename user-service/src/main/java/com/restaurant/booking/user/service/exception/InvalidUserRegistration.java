package com.restaurant.booking.user.service.exception;

public class InvalidUserRegistration extends RuntimeException {

    public InvalidUserRegistration(String message) {
        super(message);
    }
}
