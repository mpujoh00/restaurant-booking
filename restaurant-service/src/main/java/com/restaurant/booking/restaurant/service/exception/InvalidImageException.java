package com.restaurant.booking.restaurant.service.exception;

public class InvalidImageException extends RuntimeException {
    public InvalidImageException(String filename) {
        super("Couldn't upload image " + filename);
    }
}
