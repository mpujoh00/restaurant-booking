package com.restaurant.booking.booking.service.controller;

import com.restaurant.booking.booking.model.Rating;
import com.restaurant.booking.booking.model.RatingCreationRequest;
import com.restaurant.booking.booking.model.RatingStatus;
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
    ResponseEntity<Rating> createRating(@RequestBody @Valid RatingCreationRequest creationRequest);

    @PreAuthorize("hasAuthority('ROLE_CLIENT') or hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Gets all ratings from a restaurant", operationId = "getRestaurantRatings")
    @GetMapping("/restaurant/{restaurantId}")
    ResponseEntity<List<Rating>> getRestaurantRatings(@PathVariable String restaurantId);

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(description = "Gets all flagged ratings", operationId = "getFlaggedRatings")
    @GetMapping("/flag")
    ResponseEntity<List<Rating>> getFlaggedRatings();

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Flags a specified rating", operationId = "flagRating")
    @PutMapping("/{ratingId}")
    ResponseEntity<Rating> flagRating(@PathVariable(value = "ratingId") String ratingId);

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Flags a specified rating", operationId = "flagRating")
    @PutMapping("/{ratingId}/status/{ratingStatus}")
    ResponseEntity<Rating> changeRatingStatus(@PathVariable(value = "ratingId") String ratingId, @PathVariable(value = "ratingStatus") RatingStatus ratingStatus);
}
