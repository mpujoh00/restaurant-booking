package com.restaurant.booking.bookingservice.controller;

import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import com.restaurant.booking.bookingservice.service.ReservationSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
public class ReservationSlotControllerImpl implements ReservationSlotController {

    private final ReservationSlotService slotService;

    @Autowired
    public ReservationSlotControllerImpl(ReservationSlotService slotService) {
        this.slotService = slotService;
    }

    @Override
    public ResponseEntity<Void> generateRestaurantSlots(ReservSlotsCreationRequest slotsCreationRequest) {
        slotService.createRestaurantSlots(slotsCreationRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> generateRestaurantTableSlots(ReservSlotsCreationRequest slotsCreationRequest) {
        slotService.createRestaurantSlots(slotsCreationRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<LocalTime>> getRestaurantSlotsByPeople(String restaurantId, Integer numPeople, LocalDate date) {
        return ResponseEntity.ok(slotService.findAllAvailableSlotsByPeople(restaurantId, numPeople, date));
    }
}
