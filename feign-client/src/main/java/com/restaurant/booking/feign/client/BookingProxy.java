package com.restaurant.booking.feign.client;

import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "booking-service")
public interface BookingProxy {

    @PostMapping("/api/v1/reservation-slots/restaurant")
    void generateRestaurantSlots(@RequestBody ReservSlotsCreationRequest slotsCreationRequest);

    @PostMapping("/api/v1/reservation-slots/restaurant-table")
    void generateRestaurantTableSlots(@RequestBody ReservSlotsCreationRequest slotsCreationRequest);
}
