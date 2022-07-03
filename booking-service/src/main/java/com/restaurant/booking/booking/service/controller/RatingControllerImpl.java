package com.restaurant.booking.booking.service.controller;

import com.restaurant.booking.booking.model.Rating;
import com.restaurant.booking.booking.model.RatingCreationRequest;
import com.restaurant.booking.booking.model.RatingStatus;
import com.restaurant.booking.booking.service.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RatingControllerImpl implements RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingControllerImpl(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    public ResponseEntity<Rating> createRating(RatingCreationRequest creationRequest) {
        return ResponseEntity.ok(ratingService.createRating(creationRequest));
    }

    @Override
    public ResponseEntity<List<Rating>> getRestaurantRatings(String restaurantId) {
        return ResponseEntity.ok(ratingService.getRestaurantRatings(restaurantId));
    }

    @Override
    public ResponseEntity<List<Rating>> getFlaggedRatings() {
        return ResponseEntity.ok(ratingService.getFlagedRatings());
    }

    @Override
    public ResponseEntity<Rating> flagRating(String ratingId) {
        return ResponseEntity.ok(ratingService.changeRatingStatus(RatingStatus.FLAGGED, ratingId));
    }

    @Override
    public ResponseEntity<Rating> changeRatingStatus(String ratingId, RatingStatus ratingStatus) {
        return ResponseEntity.ok(ratingService.changeRatingStatus(ratingStatus, ratingId));
    }
}
