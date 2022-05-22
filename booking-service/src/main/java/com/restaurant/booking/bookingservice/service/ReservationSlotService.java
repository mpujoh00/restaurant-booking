package com.restaurant.booking.bookingservice.service;

import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import com.restaurant.booking.booking.model.ReservationSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationSlotService {

    ReservationSlot save(ReservationSlot slot);

    void createRestaurantSlots(ReservSlotsCreationRequest slotsCreationRequest);

    ReservationSlot findSlot(String slotId);

    List<LocalTime> findAllAvailableSlotsByPeople(String restaurantId, Integer numPeople, LocalDate date);

    Optional<ReservationSlot> findFirstAvailableSlotsByPeople(String restaurantId, Integer numPeople, LocalDate date);

    ReservationSlot changeStatus(ReservationSlot slot);
}
