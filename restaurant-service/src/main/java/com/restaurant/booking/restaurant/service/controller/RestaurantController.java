package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.Restaurant;
import com.restaurant.booking.restaurant.model.RestaurantRegistrationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Restaurant")
@RequestMapping("/api/v1/restaurants")
public interface RestaurantController {

    @Operation(description = "Registers restaurant and returns created restaurant", operationId = "register")
    @PostMapping("/register")
    ResponseEntity<Restaurant> register(@RequestBody @Valid RestaurantRegistrationRequest restaurantRegistrationRequest);

    @Operation(description = "Gets the restaurant whose admin email the given one", operationId = "getRestaurantByAdminEmail")
    @GetMapping("/{adminEmail}")
    ResponseEntity<Restaurant> getRestaurantByAdminEmail(@PathVariable String adminEmail);
}
