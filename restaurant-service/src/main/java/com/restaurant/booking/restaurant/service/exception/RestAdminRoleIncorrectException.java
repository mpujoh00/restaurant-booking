package com.restaurant.booking.restaurant.service.exception;

public class RestAdminRoleIncorrectException extends RuntimeException {

    public RestAdminRoleIncorrectException(String restaurantAdminEmail) {
        super("User with email " + restaurantAdminEmail + " doesn't have restaurant role");
    }
}
