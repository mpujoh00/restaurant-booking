package com.restaurant.booking.booking.service.exception;

public class RatingNotFoundException extends RuntimeException {

    public RatingNotFoundException(String ratingId) {
        super("Rating with id " + ratingId + " not found");
    }

}