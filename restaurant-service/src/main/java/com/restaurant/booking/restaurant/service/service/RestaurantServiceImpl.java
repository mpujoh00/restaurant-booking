package com.restaurant.booking.restaurant.service.service;

import com.restaurant.booking.feign.client.UserProxy;
import com.restaurant.booking.restaurant.model.Restaurant;
import com.restaurant.booking.restaurant.model.RestaurantRegistrationRequest;
import com.restaurant.booking.restaurant.service.exception.RestaurantNotFoundException;
import com.restaurant.booking.restaurant.service.repository.RestaurantRepository;
import com.restaurant.booking.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserProxy userProxy;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, UserProxy userProxy) {
        this.restaurantRepository = restaurantRepository;
        this.userProxy = userProxy;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        log.info("Saving restaurant: {}", restaurant.getName());
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant register(RestaurantRegistrationRequest restaurantRegistrationRequest) {
        log.info("Registering new restaurant: {}", restaurantRegistrationRequest.getName());

        Restaurant restaurant = new Restaurant(restaurantRegistrationRequest);

        List<LocalTime> reservationHours = new ArrayList<>();

        LocalTime currentInterval = restaurantRegistrationRequest.getOpenTime();

        while(currentInterval.isBefore(restaurantRegistrationRequest.getCloseTime())){
            reservationHours.add(currentInterval);
            currentInterval = currentInterval.plusMinutes(restaurantRegistrationRequest.getIntervalMinutes().getMinutes());
        }
        restaurant.setReservationHours(reservationHours);

        User restaurantAdmin = userProxy.getUserByEmail(restaurantRegistrationRequest.getRestaurantAdminEmail()).getBody();
        restaurant.setRestaurantAdmin(restaurantAdmin);

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant findByRestaurantAdmin(String restaurantAdminEmail) {

        log.info("Getting restaurant whose admin is {}", restaurantAdminEmail);

        User restaurantAdmin = userProxy.getUserByEmail(restaurantAdminEmail).getBody();

        return restaurantRepository.findByRestaurantAdmin(restaurantAdmin).orElseThrow(() -> new RestaurantNotFoundException(restaurantAdminEmail));
    }
}
