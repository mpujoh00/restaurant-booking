package com.restaurant.booking.bookingservice.exception;

public class ReservationSlotNotFoundException extends RuntimeException {
    public ReservationSlotNotFoundException(String slotId) {
        super("Reservation slot with id " + slotId + " not found");
    }
}
