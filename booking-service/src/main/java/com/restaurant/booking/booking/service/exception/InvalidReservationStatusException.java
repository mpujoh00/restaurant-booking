package com.restaurant.booking.booking.service.exception;

import com.restaurant.booking.booking.model.Reservation;
import com.restaurant.booking.booking.model.ReservationStatus;

public class InvalidReservationStatusException extends RuntimeException {
    public InvalidReservationStatusException(Reservation reservation, ReservationStatus status) {
        super("Invalid status " + status.name() + " for reservation with id " + reservation.getId() +
                ", valid statuses are " + ReservationStatus.getValidStatus(reservation.getStatus()));
    }
}
