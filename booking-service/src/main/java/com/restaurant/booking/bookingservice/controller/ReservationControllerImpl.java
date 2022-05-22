package com.restaurant.booking.bookingservice.controller;

import com.restaurant.booking.booking.model.Reservation;
import com.restaurant.booking.booking.model.ReservationCreationRequest;
import com.restaurant.booking.booking.model.ReservationStatus;
import com.restaurant.booking.bookingservice.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReservationControllerImpl implements ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationControllerImpl(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public ResponseEntity<Reservation> createReservation(ReservationCreationRequest creationRequest) {
        return new ResponseEntity<>(reservationService.createReservation(creationRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Reservation> getReservation(String reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @Override
    public ResponseEntity<List<Reservation>> getActiveUserReservations(String userId) {
        return ResponseEntity.ok(reservationService.findActiveUserReservations(userId));
    }

    @Override
    public ResponseEntity<List<Reservation>> getInactiveUserReservations(String userId) {
        return ResponseEntity.ok(reservationService.findInactiveUserReservations(userId));
    }

    @Override
    public ResponseEntity<Reservation> cancelReservation(String reservationId) {
        return ResponseEntity.ok(reservationService.changeReservationStatus(reservationService.findReservation(reservationId), ReservationStatus.CANCELED));
    }
}
