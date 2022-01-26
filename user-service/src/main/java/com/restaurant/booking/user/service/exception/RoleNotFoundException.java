package com.restaurant.booking.user.service.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String name) {
        super("Role " + name + " not found");
    }
}
