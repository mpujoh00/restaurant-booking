package com.restaurant.booking.restaurant.service.exception;

public class RestaurantNotFoundException extends RuntimeException {

    public RestaurantNotFoundException(String email){
        super("User's " + email + " restaurant not found");
    }
}
