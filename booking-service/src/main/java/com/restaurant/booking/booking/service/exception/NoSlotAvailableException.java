package com.restaurant.booking.booking.service.exception;

public class NoSlotAvailableException extends RuntimeException {
    public NoSlotAvailableException(Integer numPeople, String date) {
        super("No slot available for " + numPeople + " people the " + date);
    }
}
