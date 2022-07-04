package com.restaurant.booking.booking.service.controller;

import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import com.restaurant.booking.booking.service.service.ReservationSlotService;
import com.restaurant.booking.table.model.Table;
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
    public ResponseEntity<Void> generateRestaurantTableSlots(ReservSlotsCreationRequest slotsCreationRequest) {
        slotService.createRestaurantSlots(slotsCreationRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<LocalTime>> getRestaurantSlotsByPeople(String restaurantId, Integer numPeople, LocalDate date) {
        return ResponseEntity.ok(slotService.findAllAvailableSlotsByPeople(restaurantId, numPeople, date));
    }

    @Override
    public ResponseEntity<Void> deleteRestaurantTableSlots(Table table) {
        slotService.deleteTableSlots(table);
        return ResponseEntity.ok().build();
    }
}
