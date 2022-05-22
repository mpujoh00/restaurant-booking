package com.restaurant.booking.bookingservice.repository;

import com.restaurant.booking.booking.model.ReservationSlot;
import com.restaurant.booking.booking.model.ReservationSlotStatus;
import com.restaurant.booking.table.model.Table;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationSlotRepository extends MongoRepository<ReservationSlot, String> {

    List<ReservationSlot> findAllByRestaurantIdAndDateAndStatus(String restaurantId, LocalDate date, ReservationSlotStatus status);
    List<ReservationSlot> findAllByTableAndDate(Table table, LocalDate date);
}
