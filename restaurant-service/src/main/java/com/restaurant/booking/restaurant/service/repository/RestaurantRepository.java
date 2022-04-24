package com.restaurant.booking.restaurant.service.repository;

import com.restaurant.booking.restaurant.model.Restaurant;
import com.restaurant.booking.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RestaurantRepository extends MongoRepository<Restaurant, Integer> {

    Optional<Restaurant> findByRestaurantAdmin(User restaurantAdmin);
}
