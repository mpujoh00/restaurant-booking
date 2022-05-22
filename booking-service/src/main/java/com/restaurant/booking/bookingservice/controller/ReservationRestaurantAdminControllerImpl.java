package com.restaurant.booking.bookingservice.controller;

import com.restaurant.booking.booking.model.Reservation;
import com.restaurant.booking.booking.model.ReservationStatus;
import com.restaurant.booking.bookingservice.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReservationRestaurantAdminControllerImpl implements ReservationRestaurantAdminController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationRestaurantAdminControllerImpl(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public ResponseEntity<List<Reservation>> getAllRestaurantReservations(String restaurantId) {
        return ResponseEntity.ok(reservationService.findRestaurantReservations(restaurantId));
    }

    @Override
    public ResponseEntity<Reservation> changeReservationStatus(String reservationId, ReservationStatus reservationStatus) {
        return ResponseEntity.ok(reservationService.changeReservationStatus(reservationService.findReservation(reservationId), reservationStatus));
    }
}
