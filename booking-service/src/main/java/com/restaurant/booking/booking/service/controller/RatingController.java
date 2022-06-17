package com.restaurant.booking.booking.service.controller;

import com.restaurant.booking.booking.model.Rating;
import com.restaurant.booking.booking.model.RatingRequestCreation;
import com.restaurant.booking.booking.model.RestaurantRating;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Rating")
@RequestMapping("/api/v1/ratings")
public interface RatingController {

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @Operation(description = "Creates a rating", operationId = "createRating")
    @PostMapping
    ResponseEntity<Rating> createRating(@RequestBody @Valid RatingRequestCreation creationRequest);

    @PreAuthorize("hasAuthority('ROLE_CLIENT') or hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Gets all ratings from a restaurant", operationId = "getRestaurantRatings")
    @GetMapping("/restaurant/{restaurantId}")
    ResponseEntity<List<Rating>> getRestaurantRatings(@PathVariable String restaurantId);

    @PreAuthorize("hasAuthority('ROLE_CLIENT') or hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Gets the average rating from a restaurant", operationId = "getRestaurantAverageRating")
    @GetMapping("/restaurant/{restaurantId}/rating")
    ResponseEntity<RestaurantRating> getRestaurantAverageRating(@PathVariable String restaurantId);

}
