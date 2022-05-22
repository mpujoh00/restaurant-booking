package com.restaurant.booking.bookingservice.exception;

public class NoSlotAvailableException extends RuntimeException {
    public NoSlotAvailableException(Integer numPeople, String date) {
        super("No slot available for " + numPeople + " people the " + date);
    }
}
