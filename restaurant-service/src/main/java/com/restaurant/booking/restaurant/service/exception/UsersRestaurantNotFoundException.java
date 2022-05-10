package com.restaurant.booking.restaurant.service.exception;

public class UsersRestaurantNotFoundException extends RuntimeException{

    public UsersRestaurantNotFoundException(String email){
        super("User's " + email + " restaurant not found");
    }
}
