package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.Restaurant;
import com.restaurant.booking.restaurant.model.RestaurantHoursUpdateRequest;
import com.restaurant.booking.restaurant.model.RestaurantRegistrationRequest;
import com.restaurant.booking.restaurant.model.RestaurantUpdateRequest;
import com.restaurant.booking.restaurant.service.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Override
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.findAllRestaurants());
    }

    @Override
    public ResponseEntity<Void> deleteRestaurantByAdminEmail(String adminEmail) {
        restaurantService.deleteByRestaurantAdmin(adminEmail);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Restaurant> updateRestaurantByAdminEmail(RestaurantUpdateRequest restaurantUpdateRequest) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(restaurantUpdateRequest));
    }

    @Override
    public ResponseEntity<Restaurant> updateRestOpenHoursByAdminEmail(RestaurantHoursUpdateRequest restaurantHoursUpdateRequest) {
        return ResponseEntity.ok(restaurantService.updateRestaurantOpeningHours(restaurantHoursUpdateRequest));
    }
}
