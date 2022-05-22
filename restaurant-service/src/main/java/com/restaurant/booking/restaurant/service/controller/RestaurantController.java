package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.Restaurant;
import com.restaurant.booking.restaurant.model.RestaurantRegistrationRequest;
import com.restaurant.booking.restaurant.model.RestaurantReservHoursUpdateRequest;
import com.restaurant.booking.restaurant.model.RestaurantUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalTime;
import java.util.List;

@Tag(name = "Restaurant")
@RequestMapping("/api/v1/restaurants")
public interface RestaurantController {

    @Operation(description = "Registers restaurant and returns created restaurant", operationId = "register")
    @PostMapping("/register")
    ResponseEntity<Restaurant> register(@RequestBody @Valid RestaurantRegistrationRequest restaurantRegistrationRequest);

    @Operation(description = "Gets the restaurant given its id", operationId = "getRestaurant")
    @GetMapping("/{restaurantId}")
    ResponseEntity<Restaurant> getRestaurant(@PathVariable String restaurantId);

    @Operation(description = "Gets all enabled restaurants", operationId = "getEnabledRestaurants")
    @GetMapping
    ResponseEntity<List<Restaurant>> getEnabledRestaurants();

    @Operation(description = "Gets the restaurant's reservation hours", operationId = "getRestaurantsReservationHours")
    @GetMapping("/reservation-hours/{restaurantId}")
    ResponseEntity<List<LocalTime>> getRestaurantsReservationHours(@PathVariable String restaurantId);

    @Operation(description = "Deletes the restaurant", operationId = "deleteRestaurant")
    @DeleteMapping("/delete/{restaurantId}")
    ResponseEntity<Void> deleteRestaurant(@PathVariable String restaurantId);

    @Operation(description = "Updates the restaurant", operationId = "updateRestaurant")
    @PutMapping
    ResponseEntity<Restaurant> updateRestaurant(@RequestBody @Valid RestaurantUpdateRequest restaurantUpdateRequest);

    @Operation(description = "Updates the restaurant's reservation hours", operationId = "updateRestaurantReservationHours")
    @PutMapping("/change-reservation-hours")
    ResponseEntity<Restaurant> updateRestaurantReservationHours(@RequestBody @Valid RestaurantReservHoursUpdateRequest restaurantReservHoursUpdateRequest);
}
