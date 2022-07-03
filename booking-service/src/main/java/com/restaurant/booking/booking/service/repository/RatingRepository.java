package com.restaurant.booking.booking.service.repository;

import com.restaurant.booking.booking.model.Rating;
import com.restaurant.booking.booking.model.RatingStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RatingRepository extends MongoRepository<Rating, String> {

    List<Rating> findAllByRestaurantId(String restaurantId);

    List<Rating> findAllByRatingStatus(RatingStatus flagged);
}
