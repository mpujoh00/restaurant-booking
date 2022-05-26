package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.Restaurant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "RestaurantAdministration")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/api/v1/admin/restaurants")
public interface RestaurantAdministrationController {

    @Operation(description = "Gets all the restaurants", operationId = "getAllRestaurants")
    @GetMapping
    ResponseEntity<List<Restaurant>> getAllRestaurants();

    @Operation(description = "Gets all disabled restaurants", operationId = "getDisabledRestaurants")
    @GetMapping("/disabled")
    ResponseEntity<List<Restaurant>> getDisabledRestaurants();

    @Operation(description = "Gets all pending restaurants", operationId = "getPendingRestaurants")
    @GetMapping("/pending")
    ResponseEntity<List<Restaurant>> getPendingRestaurants();

    @Operation(description = "Updates the status of a restaurant", operationId = "updateRestaurantStatus")
    @PutMapping("/change-status/{restaurantId}")
    ResponseEntity<Restaurant> updateRestaurantStatus(@PathVariable String restaurantId);
}
