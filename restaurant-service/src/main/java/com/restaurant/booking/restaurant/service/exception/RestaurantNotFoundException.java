package com.restaurant.booking.restaurant.service.exception;

public class RestaurantNotFoundException extends RuntimeException {

    public RestaurantNotFoundException(String id){
        super("Restaurant with id " + id + " not found");
    }
}
