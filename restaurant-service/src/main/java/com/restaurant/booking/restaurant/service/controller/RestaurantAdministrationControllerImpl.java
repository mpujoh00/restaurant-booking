package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.Restaurant;
import com.restaurant.booking.restaurant.model.RestaurantStatus;
import com.restaurant.booking.restaurant.service.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class RestaurantAdministrationControllerImpl implements RestaurantAdministrationController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantAdministrationControllerImpl(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Override
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.findAllRestaurants());
    }

    @Override
    public ResponseEntity<List<Restaurant>> getDisabledRestaurants() {
        return ResponseEntity.ok(restaurantService.findRestaurantsByStatus(RestaurantStatus.DISABLED));
    }

    @Override
    public ResponseEntity<List<Restaurant>> getPendingRestaurants() {
        return ResponseEntity.ok(restaurantService.findRestaurantsByStatus(RestaurantStatus.PENDING));
    }

    @Override
    public ResponseEntity<Restaurant> updateRestaurantStatus(String restaurantId) {
        return ResponseEntity.ok(restaurantService.changeRestaurantStatus(restaurantService.findRestaurant(restaurantId)));
    }
}
