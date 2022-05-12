package com.restaurant.booking.restaurant.service.repository;

import com.restaurant.booking.restaurant.model.Restaurant;
import com.restaurant.booking.restaurant.model.RestaurantStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

@DataMongoTest
class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @AfterEach
    void cleanUp(){
        restaurantRepository.deleteAll();
    }

    @Test
    void findByStatus(){

        List<Restaurant> restaurants = List.of(Restaurant.builder().id("id").status(RestaurantStatus.ENABLED).build(),
                Restaurant.builder().id("id2").status(RestaurantStatus.DISABLED).build());

        restaurantRepository.saveAll(restaurants);

        List<Restaurant> obtainedRestaurants = restaurantRepository.findByStatus(RestaurantStatus.DISABLED);

        Assertions.assertEquals(List.of(restaurants.get(1)), obtainedRestaurants);
    }
}
