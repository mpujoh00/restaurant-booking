package com.restaurant.booking.bookingservice.exception;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(String reservationId) {
        super("Reservation with id " + reservationId + " not found");
    }
}
