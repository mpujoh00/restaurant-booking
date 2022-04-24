package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.Restaurant;
import com.restaurant.booking.restaurant.model.RestaurantRegistrationRequest;
import com.restaurant.booking.restaurant.service.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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
    public ResponseEntity<Restaurant> getRestaurantByAdminEmail(String adminEmail) {
        return ResponseEntity.ok(restaurantService.findByRestaurantAdmin(adminEmail));
    }
}
