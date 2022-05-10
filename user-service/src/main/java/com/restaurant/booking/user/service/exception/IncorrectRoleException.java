package com.restaurant.booking.user.service.exception;

public class IncorrectRoleException extends RuntimeException {
    public IncorrectRoleException(String email, String role) {
        super("User with email " + email + " doesn't have " + role + " role");
    }
}
