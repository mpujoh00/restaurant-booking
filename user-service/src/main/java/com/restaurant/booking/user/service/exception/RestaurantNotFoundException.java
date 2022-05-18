package com.restaurant.booking.user.service.exception;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(String restaurantId) {
        super("Restaurant with id " + restaurantId + " not found");
    }
}
