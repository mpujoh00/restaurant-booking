package com.restaurant.booking.feign.client.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super("Bad request");
    }
}
