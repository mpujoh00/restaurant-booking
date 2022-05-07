package com.restaurant.booking.restaurant.service.service;

import com.restaurant.booking.feign.client.UserProxy;
import com.restaurant.booking.feign.client.exception.NotFoundException;
import com.restaurant.booking.restaurant.model.*;
import com.restaurant.booking.restaurant.service.exception.RestAdminRoleIncorrectException;
import com.restaurant.booking.restaurant.service.exception.RestaurantAlreadyExistsException;
import com.restaurant.booking.restaurant.service.exception.RestaurantNotFoundException;
import com.restaurant.booking.restaurant.service.exception.UserNotFoundException;
import com.restaurant.booking.restaurant.service.repository.RestaurantRepository;
import com.restaurant.booking.user.model.RoleName;
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

        restaurant.setReservationHours(
                getReservationHours(restaurantRegistrationRequest.getOpenTime(), restaurantRegistrationRequest.getCloseTime(), restaurantRegistrationRequest.getIntervalMinutes()));

        User restaurantAdmin = getRestaurantAdmin(restaurantRegistrationRequest.getRestaurantAdminEmail());
        // check if admin already has a restaurant (a user can only be the administrator of one restaurant)
        if(restaurantRepository.findByRestaurantAdmin(restaurantAdmin).isPresent()){
            log.error("User with email {} already has a restaurant", restaurantRegistrationRequest.getRestaurantAdminEmail());
            throw new RestaurantAlreadyExistsException(restaurantAdmin.getEmail());
        }
        restaurant.setRestaurantAdmin(restaurantAdmin);

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant findByRestaurantAdmin(String restaurantAdminEmail) {

        log.info("Getting restaurant whose admin is {}", restaurantAdminEmail);
        return restaurantRepository.findByRestaurantAdmin(getRestaurantAdmin(restaurantAdminEmail))
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantAdminEmail));
    }

    @Override
    public Restaurant findRestaurant(String restaurantId) {

        log.info("Getting restaurant with id {}", restaurantId);
        return restaurantRepository.findById(restaurantId).orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
    }

    @Override
    public List<Restaurant> findAllRestaurants() {

        log.info("Getting all restaurants");
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> findRestaurantsByStatus(RestaurantStatus restaurantStatus) {

        log.info("Getting all {} restaurants", restaurantStatus);
        return restaurantRepository.findByStatus(restaurantStatus);
    }

    @Override
    public void deleteRestaurant(String restaurantId) {

        log.info("Deleting restaurant with id {}", restaurantId);
        restaurantRepository.deleteById(restaurantId);
    }

    @Override
    public Restaurant updateRestaurant(RestaurantUpdateRequest restaurantUpdateRequest) {

        log.info("Updating restaurant with id {}", restaurantUpdateRequest.getRestaurantId());

        Restaurant restaurant = findRestaurant(restaurantUpdateRequest.getRestaurantId());

        if(restaurantUpdateRequest.getLocation() != null)
            restaurant.setLocation(restaurantUpdateRequest.getLocation());
        if(restaurantUpdateRequest.getName() != null)
            restaurant.setName(restaurantUpdateRequest.getName());

        return save(restaurant);
    }

    @Override
    public Restaurant updateRestaurantReservationHours(RestaurantReservHoursUpdateRequest restaurantReservHoursUpdateRequest) {

        log.info("Updating restaurant with id {}", restaurantReservHoursUpdateRequest.getRestaurantId());

        Restaurant restaurant = findRestaurant(restaurantReservHoursUpdateRequest.getRestaurantId());
        restaurant.setReservationHours(
                getReservationHours(restaurantReservHoursUpdateRequest.getOpenTime(), restaurantReservHoursUpdateRequest.getCloseTime(), restaurantReservHoursUpdateRequest.getIntervalMinutes()));
        return save(restaurant);
    }

    @Override
    public Restaurant changeRestaurantStatus(Restaurant restaurant) {

        log.info("Changing restaurant's with id {} status", restaurant.getId());

        restaurant.setStatus(restaurant.getStatus().nextStatus());
        return save(restaurant);
    }

    private User getRestaurantAdmin(String restaurantAdminEmail){

        User restaurantAdmin;
        try{
            restaurantAdmin = userProxy.getUserByEmail(restaurantAdminEmail);
        }
        catch (NotFoundException e){
            log.error("User with email {} doesn't exist", restaurantAdminEmail);
            throw new UserNotFoundException(restaurantAdminEmail);
        }
        // checks if the user has role restaurant
        if(restaurantAdmin.getRolesList().get(0).getName().equals(RoleName.ROLE_RESTAURANT)) {
            return restaurantAdmin;
        } else{
            log.error("User with email {} doesn't have restaurant role", restaurantAdminEmail);
            throw new RestAdminRoleIncorrectException(restaurantAdmin.getEmail());
        }
    }

    private List<LocalTime> getReservationHours(LocalTime openTime, LocalTime closeTime, Interval interval){

        List<LocalTime> reservationHours = new ArrayList<>();
        LocalTime currentInterval = openTime;

        while(currentInterval.isBefore(closeTime) &&
                (currentInterval.plusMinutes(interval.getMinutes()).isBefore(closeTime) || currentInterval.plusMinutes(interval.getMinutes()).equals(closeTime))){
            reservationHours.add(currentInterval);
            currentInterval = currentInterval.plusMinutes(interval.getMinutes());
        }
        return reservationHours;
    }
}
