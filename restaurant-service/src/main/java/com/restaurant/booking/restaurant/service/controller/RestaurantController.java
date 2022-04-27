package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.Restaurant;
import com.restaurant.booking.restaurant.model.RestaurantHoursUpdateRequest;
import com.restaurant.booking.restaurant.model.RestaurantRegistrationRequest;
import com.restaurant.booking.restaurant.model.RestaurantUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Restaurant")
@RequestMapping("/api/v1/restaurants")
public interface RestaurantController {

    @Operation(description = "Registers restaurant and returns created restaurant", operationId = "register")
    @PostMapping("/register")
    ResponseEntity<Restaurant> register(@RequestBody @Valid RestaurantRegistrationRequest restaurantRegistrationRequest);

    @Operation(description = "Gets the restaurant given its admin's email", operationId = "getRestaurantByAdminEmail")
    @GetMapping("/{adminEmail}")
    ResponseEntity<Restaurant> getRestaurantByAdminEmail(@PathVariable String adminEmail);

    @Operation(description = "Gets all the restaurants", operationId = "getAllRestaurants")
    @GetMapping
    ResponseEntity<List<Restaurant>> getAllRestaurants();

    @Operation(description = "Deletes the restaurant given its admin's email", operationId = "deleteRestaurantByAdminEmail")
    @DeleteMapping("/delete/{adminEmail}")
    ResponseEntity<Void> deleteRestaurantByAdminEmail(@PathVariable String adminEmail);

    @Operation(description = "Updates the restaurant given its admin's email", operationId = "updateRestaurantByAdminEmail")
    @PutMapping
    ResponseEntity<Restaurant> updateRestaurantByAdminEmail(@RequestBody @Valid RestaurantUpdateRequest restaurantUpdateRequest);

    @Operation(description = "Updates the restaurant's opening hours given its admin's email", operationId = "updateRestOpenHoursByAdminEmail")
    @PutMapping("/change-opening-hours")
    ResponseEntity<Restaurant> updateRestOpenHoursByAdminEmail(@RequestBody @Valid RestaurantHoursUpdateRequest restaurantHoursUpdateRequest);
}
