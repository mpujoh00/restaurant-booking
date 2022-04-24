package com.restaurant.booking.restaurant.service.service;

import com.restaurant.booking.restaurant.model.Restaurant;
import com.restaurant.booking.restaurant.model.RestaurantRegistrationRequest;

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
}
