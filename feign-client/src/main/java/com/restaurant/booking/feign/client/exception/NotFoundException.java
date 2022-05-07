package com.restaurant.booking.feign.client.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Not found");
    }
}