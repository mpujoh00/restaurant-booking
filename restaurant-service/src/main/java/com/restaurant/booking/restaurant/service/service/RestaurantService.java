package com.restaurant.booking.restaurant.service.service;

import com.restaurant.booking.restaurant.model.*;

import java.time.LocalTime;
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
     * @param restaurant
     * @return
     */
    List<LocalTime> getRestaurantsReservationHours(Restaurant restaurant);

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
}
