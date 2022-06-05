package com.restaurant.booking.booking.service.controller;

import com.restaurant.booking.booking.model.Reservation;
import com.restaurant.booking.booking.model.ReservationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Tag(name = "ReservationAdmin")
@RequestMapping("/api/v1/admin/reservations")
public interface ReservationRestaurantAdminController {

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Gets all restaurant's reservations", operationId = "getAllRestaurantReservations")
    @GetMapping("/restaurant/{restaurantId}")
    ResponseEntity<List<Reservation>> getAllRestaurantReservations(@PathVariable String restaurantId);

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Changes a reservation's status", operationId = "changeReservationStatus")
    @PutMapping("/{reservationId}/status/{reservationStatus}")
    ResponseEntity<Reservation> changeReservationStatus(@PathVariable String reservationId, @PathVariable ReservationStatus reservationStatus);
}
