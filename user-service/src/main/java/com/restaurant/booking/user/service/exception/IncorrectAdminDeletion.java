package com.restaurant.booking.user.service.exception;

public class IncorrectAdminDeletion extends RuntimeException {
    public IncorrectAdminDeletion(String userEmail) {
        super("Can't delete user with email " + userEmail + ", must have admin role");
    }
}
