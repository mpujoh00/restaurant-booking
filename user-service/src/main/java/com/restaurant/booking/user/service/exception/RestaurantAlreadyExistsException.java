package com.restaurant.booking.user.service.exception;

public class RestaurantAlreadyExistsException extends RuntimeException {
    public RestaurantAlreadyExistsException(String email) {
        super("Can't add restaurant, user with email " + email + " already has a restaurant");
    }
}
