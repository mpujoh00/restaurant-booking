package com.restaurant.booking.feign.client;

import com.restaurant.booking.restaurant.model.Restaurant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalTime;
import java.util.List;

@FeignClient(name = "restaurant-service")
public interface RestaurantProxy {

    @GetMapping("/api/v1/restaurants/{restaurantId}")
    Restaurant getRestaurant(@PathVariable(value = "restaurantId") String restaurantId);

    @GetMapping("/api/v1/restaurants/reservation-hours/{restaurantId}")
    List<LocalTime> getRestaurantsReservationHours(@PathVariable(value = "restaurantId") String restaurantId);
}
