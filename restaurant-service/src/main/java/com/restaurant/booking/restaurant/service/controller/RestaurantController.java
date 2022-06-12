package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_CLIENT')")
    @Operation(description = "Gets all enabled restaurants", operationId = "getEnabledRestaurants")
    @GetMapping
    ResponseEntity<List<Restaurant>> getEnabledRestaurants();

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT') or hasAuthority('ROLE_CLIENT')")
    @Operation(description = "Gets the restaurant's reservation hours", operationId = "getRestaurantsReservationHours")
    @GetMapping("/reservation-hours/{restaurantId}")
    ResponseEntity<List<LocalTime>> getRestaurantsReservationHours(@PathVariable String restaurantId);

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Deletes the restaurant", operationId = "deleteRestaurant")
    @DeleteMapping("/delete/{restaurantId}")
    ResponseEntity<Void> deleteRestaurant(@PathVariable String restaurantId);

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Updates the restaurant", operationId = "updateRestaurant")
    @PutMapping
    ResponseEntity<Restaurant> updateRestaurant(@RequestBody @Valid RestaurantUpdateRequest restaurantUpdateRequest);

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Updates the restaurant's reservation hours", operationId = "updateRestaurantReservationHours")
    @PutMapping("/change-reservation-hours")
    ResponseEntity<Restaurant> updateRestaurantReservationHours(@RequestBody @Valid RestaurantReservHoursUpdateRequest restaurantReservHoursUpdateRequest);

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Adds a new category to the restaurant", operationId = "addCategory")
    @PutMapping("/{restaurantId}/add-category")
    ResponseEntity<Restaurant> addCategory(@PathVariable String restaurantId, @RequestBody String categoryName);

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Removes a category from the restaurant", operationId = "removeCategory")
    @PutMapping("/{restaurantId}/remove-category")
    ResponseEntity<Restaurant> removeCategory(@PathVariable String restaurantId, @RequestBody String categoryName);

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @Operation(description = "Gets all restaurants given the parameters", operationId = "searchRestaurants")
    @GetMapping("/search")
    ResponseEntity<List<Restaurant>> searchRestaurants(@RequestBody @Valid SearchRestaurantsRequest searchRestaurantsRequest);

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Updates restaurant's logo", operationId = "saveLogo")
    @PutMapping("/{restaurantId}/logo")
    ResponseEntity<Restaurant> saveLogo(@PathVariable String restaurantId, @RequestParam("file") MultipartFile logo);
}
