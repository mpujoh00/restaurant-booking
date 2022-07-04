package com.restaurant.booking.feign.client;

import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import com.restaurant.booking.table.model.Table;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "booking-service")
public interface BookingProxy {

    @PostMapping("/api/v1/reservation-slots/restaurant-table")
    void generateRestaurantTableSlots(@RequestHeader(value = "Authorization") String authorizationHeader,
                                      @RequestBody ReservSlotsCreationRequest slotsCreationRequest);

    @DeleteMapping("/api/v1/reservation-slots/restaurant-table")
    void deleteRestaurantTableSlots(@RequestHeader(value = "Authorization") String authorizationHeader,
                                    @RequestBody Table table);
}
