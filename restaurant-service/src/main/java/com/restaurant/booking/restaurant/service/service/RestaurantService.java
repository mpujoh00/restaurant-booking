package com.restaurant.booking.restaurant.service.service;

import com.restaurant.booking.restaurant.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RestaurantService {

    /**
     *
     * @param restaurant
     * @return
     */
    Restaurant save(Restaurant restaurant);

    /**
     *
     * @param restaurantRegistrationRequest
     * @return
     */
    Restaurant register(RestaurantRegistrationRequest restaurantRegistrationRequest);

    /**
     *
     * @param restaurantId
     * @return
     */
    Restaurant findRestaurant(String restaurantId);

    /**
     *
     * @return
     */
    List<Restaurant> findAllRestaurants();

    /**
     *
     * @return
     */
    List<Restaurant> findRestaurantsByStatus(RestaurantStatus restaurantStatus) ;

    /**
     *
     * @param restaurantId
     * @return
     */
    void deleteRestaurant(String restaurantId);

    /**
     *
     * @param restaurantUpdateRequest
     * @return
     */
    Restaurant updateRestaurant(RestaurantUpdateRequest restaurantUpdateRequest);

    /**
     *
     * @param restaurantReservHoursUpdateRequest
     * @return
     */
    Restaurant updateRestaurantReservationHours(RestaurantReservHoursUpdateRequest restaurantReservHoursUpdateRequest);

    /**
     *
     * @param restaurant
     * @return
     */
    Restaurant changeRestaurantStatus(Restaurant restaurant);

    Restaurant addCategory(Restaurant restaurant, Category category);

    Restaurant removeCategory(Restaurant restaurant, Category category);

    List<Restaurant> searchRestaurants(SearchRestaurantsRequest searchRestaurantsRequest);

    Restaurant saveRestaurantLogo(Restaurant restaurant, MultipartFile logo);

    void updateRestaurantRating(AverageRatingUpdateRequest ratingUpdateRequest);
}
