package com.restaurant.booking.booking.service.controller;

import com.restaurant.booking.booking.model.Reservation;
import com.restaurant.booking.booking.model.ReservationCreationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Reservation")
@RequestMapping("/api/v1/reservations")
public interface ReservationController {

    @Operation(description = "Creates a reservation", operationId = "createReservation")
    @PostMapping
    ResponseEntity<Reservation> createReservation(@RequestBody ReservationCreationRequest creationRequest);

    @Operation(description = "Gets a reservation", operationId = "getReservation")
    @GetMapping("/{reservationId}")
    ResponseEntity<Reservation> getReservation(@PathVariable String reservationId);

    @Operation(description = "Gets active reservations", operationId = "getAllUserReservations")
    @GetMapping("/user/active/{userId}")
    ResponseEntity<List<Reservation>> getActiveUserReservations(@PathVariable String userId);

    @Operation(description = "Gets past reservations", operationId = "getAllUserReservations")
    @GetMapping("/user/inactive/{userId}")
    ResponseEntity<List<Reservation>> getInactiveUserReservations(@PathVariable String userId);

    @Operation(description = "Changes a reservation's status", operationId = "changeReservationStatus")
    @PutMapping("/{reservationId}")
    ResponseEntity<Reservation> cancelReservation(@PathVariable String reservationId);
}
