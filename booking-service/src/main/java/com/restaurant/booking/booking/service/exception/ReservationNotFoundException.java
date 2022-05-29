package com.restaurant.booking.booking.service.exception;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(String reservationId) {
        super("Reservation with id " + reservationId + " not found");
    }
}
