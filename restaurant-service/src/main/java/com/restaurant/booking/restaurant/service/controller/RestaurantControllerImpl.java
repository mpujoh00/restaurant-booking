package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.*;
import com.restaurant.booking.restaurant.service.service.CategoryService;
import com.restaurant.booking.restaurant.service.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.List;

@RestController
public class RestaurantControllerImpl implements RestaurantController {

    private final RestaurantService restaurantService;
    private final CategoryService categoryService;

    @Autowired
    public RestaurantControllerImpl(RestaurantService restaurantService, CategoryService categoryService) {
        this.restaurantService = restaurantService;
        this.categoryService = categoryService;
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
    public ResponseEntity<List<LocalTime>> getRestaurantsReservationHours(String restaurantId) {
        return ResponseEntity.ok(restaurantService.findRestaurant(restaurantId).getReservationHours());
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

    @Override
    public ResponseEntity<Restaurant> addCategory(String restaurantId, String categoryName) {
        return ResponseEntity.ok(restaurantService.addCategory(restaurantService.findRestaurant(restaurantId), categoryService.getCategory(categoryName)));
    }

    @Override
    public ResponseEntity<Restaurant> removeCategory(String restaurantId, String categoryName) {
        return ResponseEntity.ok(restaurantService.removeCategory(restaurantService.findRestaurant(restaurantId), categoryService.getCategory(categoryName)));
    }

    @Override
    public ResponseEntity<List<Restaurant>> searchRestaurants(SearchRestaurantsRequest searchRestaurantsRequest) {
        return ResponseEntity.ok(restaurantService.searchRestaurants(searchRestaurantsRequest));
    }

    @Override
    public ResponseEntity<Restaurant> saveLogo(String restaurantId, MultipartFile logo) {
        return ResponseEntity.ok(restaurantService.saveRestaurantLogo(restaurantService.findRestaurant(restaurantId), logo));
    }
}
