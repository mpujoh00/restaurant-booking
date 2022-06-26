package com.restaurant.booking.booking.service.service;

import com.restaurant.booking.booking.model.Rating;
import com.restaurant.booking.booking.model.RatingCreationRequest;
import com.restaurant.booking.booking.model.RatingStatus;

import java.util.List;

public interface RatingService {

    Rating save(Rating rating);

    Rating createRating(RatingCreationRequest requestCreation);

    List<Rating> getRestaurantRatings(String restaurantId);

    List<Rating> getFlagedRatings();

    Rating changeRatingStatus(RatingStatus ratingStatus, String ratingId);
}
