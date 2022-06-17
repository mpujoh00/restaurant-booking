package com.restaurant.booking.booking.service.service;

import com.restaurant.booking.booking.model.Rating;
import com.restaurant.booking.booking.model.RatingRequestCreation;
import com.restaurant.booking.booking.model.RestaurantRating;

import java.util.List;

public interface RatingService {

    Rating save(Rating rating);

    Rating createRating(RatingRequestCreation requestCreation);

    List<Rating> getRestaurantRatings(String restaurantId);

    RestaurantRating getRestaurantAverageRating(String restaurantId);
}
