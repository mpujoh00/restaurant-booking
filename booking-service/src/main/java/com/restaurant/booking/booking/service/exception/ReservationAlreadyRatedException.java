package com.restaurant.booking.booking.service.exception;

public class ReservationAlreadyRatedException extends RuntimeException {
    public ReservationAlreadyRatedException(String reservationId) {
        super("Reservation with id " + reservationId + " has already been rated");
    }
}
