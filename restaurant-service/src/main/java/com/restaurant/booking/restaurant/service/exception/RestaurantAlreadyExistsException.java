package com.restaurant.booking.restaurant.service.exception;

public class RestaurantAlreadyExistsException extends RuntimeException {

    public RestaurantAlreadyExistsException(String restaurantAdminEmail) {
        super("User with email " + restaurantAdminEmail + " already has a restaurant");
    }
}
