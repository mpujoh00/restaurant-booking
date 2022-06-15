package com.restaurant.booking.restaurant.service.repository;

import com.restaurant.booking.restaurant.model.Category;
import com.restaurant.booking.restaurant.model.Restaurant;
import com.restaurant.booking.restaurant.model.RestaurantStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RestaurantRepository extends MongoRepository<Restaurant, String> {

    List<Restaurant> findByStatus(RestaurantStatus restaurantStatus);

    List<Restaurant> findAllByLocationIgnoreCase(String location);

    List<Restaurant> findAllByLocationIgnoreCaseAndCategoriesIn(String location, List<Category> categories);

    List<Restaurant> findAllByCategoriesIn(List<Category> categories);
}
