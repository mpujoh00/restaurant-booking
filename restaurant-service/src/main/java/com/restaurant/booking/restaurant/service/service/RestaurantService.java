package com.restaurant.booking.restaurant.service.service;

import com.restaurant.booking.restaurant.model.Restaurant;
import com.restaurant.booking.restaurant.model.RestaurantHoursUpdateRequest;
import com.restaurant.booking.restaurant.model.RestaurantRegistrationRequest;
import com.restaurant.booking.restaurant.model.RestaurantUpdateRequest;

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
     * @param restaurantAdminEmail
     * @return
     */
    Restaurant findByRestaurantAdmin(String restaurantAdminEmail);

    /**
     *
     * @return
     */
    List<Restaurant> findAllRestaurants();

    /**
     *
     * @param restaurantAdminEmail
     * @return
     */
    void deleteByRestaurantAdmin(String restaurantAdminEmail);

    /**
     *
     * @param restaurantUpdateRequest
     * @return
     */
    Restaurant updateRestaurant(RestaurantUpdateRequest restaurantUpdateRequest);

    /**
     *
     * @param restaurantHoursUpdateRequest
     * @return
     */
    Restaurant updateRestaurantOpeningHours(RestaurantHoursUpdateRequest restaurantHoursUpdateRequest);
}
