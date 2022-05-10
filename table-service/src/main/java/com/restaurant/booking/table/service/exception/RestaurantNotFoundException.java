package com.restaurant.booking.table.service.exception;

public class RestaurantNotFoundException extends RuntimeException {

    public RestaurantNotFoundException(String id){
        super("Restaurant with id " + id + " not found");
    }
}
