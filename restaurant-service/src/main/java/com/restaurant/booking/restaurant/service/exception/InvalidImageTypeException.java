package com.restaurant.booking.restaurant.service.exception;

public class InvalidImageTypeException extends RuntimeException {
    public InvalidImageTypeException(String filename) {
        super("Couldn't upload image" + filename + ", valid extensions are: *.jpg, *.png, *.jpeg");
    }
}
