package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.*;
import com.restaurant.booking.restaurant.service.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestaurantControllerImpl implements RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantControllerImpl(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Override
    public ResponseEntity<Restaurant> register(RestaurantRegistrationRequest restaurantRegistrationRequest) {
        return new ResponseEntity<>(restaurantService.register(restaurantRegistrationRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Restaurant> getRestaurant(String restaurantId) {
        return ResponseEntity.ok(restaurantService.findRestaurant(restaurantId));
    }

    @Override
    public ResponseEntity<List<Restaurant>> getEnabledRestaurants() {
        return ResponseEntity.ok(restaurantService.findRestaurantsByStatus(RestaurantStatus.ENABLED));
    }

    @Override
    public ResponseEntity<Void> deleteRestaurant(String restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Restaurant> updateRestaurant(RestaurantUpdateRequest restaurantUpdateRequest) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(restaurantUpdateRequest));
    }

    @Override
    public ResponseEntity<Restaurant> updateRestaurantReservationHours(RestaurantReservHoursUpdateRequest restaurantReservHoursUpdateRequest) {
        return ResponseEntity.ok(restaurantService.updateRestaurantReservationHours(restaurantReservHoursUpdateRequest));
    }
}
