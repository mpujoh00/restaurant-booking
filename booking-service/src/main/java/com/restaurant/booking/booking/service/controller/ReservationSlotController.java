package com.restaurant.booking.booking.service.controller;

import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import com.restaurant.booking.table.model.Table;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Tag(name = "ReservationSlot")
@RequestMapping("/api/v1/reservation-slots")
public interface ReservationSlotController {

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Creates the reservation slots for a restaurant", operationId = "generateRestaurantSlots")
    @PostMapping("/restaurant")
    ResponseEntity<Void> generateRestaurantSlots(@RequestBody ReservSlotsCreationRequest slotsCreationRequest);

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Creates the reservation slots for a restaurant's table", operationId = "generateRestaurantTableSlots")
    @PostMapping("/restaurant-table")
    ResponseEntity<Void> generateRestaurantTableSlots(@RequestBody ReservSlotsCreationRequest slotsCreationRequest);

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @Operation(description = "Gets all reservation slots for a restaurant given the number of people", operationId = "getRestaurantSlotsByPeople")
    @GetMapping("/restaurant/{restaurantId}/people/{numPeople}/date/{date}")
    ResponseEntity<List<LocalTime>> getRestaurantSlotsByPeople(@PathVariable String restaurantId,
                                                               @PathVariable Integer numPeople,
                                                               @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Deletes all reservation slots of a restaurant's table", operationId = "deleteRestaurantTableSlots")
    @DeleteMapping("/restaurant-table")
    ResponseEntity<Void> deleteRestaurantTableSlots(@RequestBody Table table);
}
