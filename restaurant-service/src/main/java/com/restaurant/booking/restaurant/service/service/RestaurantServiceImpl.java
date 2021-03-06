package com.restaurant.booking.restaurant.service.service;

import com.restaurant.booking.feign.client.UserProxy;
import com.restaurant.booking.feign.client.exception.BadRequestException;
import com.restaurant.booking.feign.client.exception.NotFoundException;
import com.restaurant.booking.jwt.utils.JwtUtils;
import com.restaurant.booking.restaurant.model.*;
import com.restaurant.booking.restaurant.service.exception.*;
import com.restaurant.booking.restaurant.service.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserProxy userProxy;
    private final JwtUtils jwtUtils;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, UserProxy userProxy, JwtUtils jwtUtils) {
        this.restaurantRepository = restaurantRepository;
        this.userProxy = userProxy;
        this.jwtUtils = jwtUtils;
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
        restaurant.setRestaurantAdminEmail(restaurantRegistrationRequest.getRestaurantAdminEmail());
        restaurant.setReservationHours(
                getReservationHours(restaurantRegistrationRequest.getOpenTime(), restaurantRegistrationRequest.getCloseTime(), restaurantRegistrationRequest.getIntervalMinutes()));
        restaurant = save(restaurant);

        // add restaurant to admin user
        try{
            userProxy.addRestaurant(jwtUtils.getAuthorizationHeader(), restaurantRegistrationRequest.getRestaurantAdminEmail(), restaurant.getId());
        } catch(BadRequestException e){
            log.error("Can't create a restaurant for user with email {}", restaurantRegistrationRequest.getRestaurantAdminEmail());
            restaurantRepository.delete(restaurant);
            throw new RestaurantAlreadyExistsException(restaurantRegistrationRequest.getRestaurantAdminEmail());
        } catch (NotFoundException e){
            log.error("User with email {} doesn't exist", restaurantRegistrationRequest.getRestaurantAdminEmail());
            restaurantRepository.delete(restaurant);
            throw new UserNotFoundException(restaurantRegistrationRequest.getRestaurantAdminEmail());
        }

        return restaurant;
    }

    @Override
    @Cacheable(value = "restaurant")
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
    @CacheEvict(value = "restaurant")
    public void deleteRestaurant(String restaurantId) {

        log.info("Deleting restaurant with id {}", restaurantId);
        restaurantRepository.deleteById(restaurantId);
    }

    @Override
    @CachePut(value = "restaurant", key = "#restaurantUpdateRequest.restaurantId")
    public Restaurant updateRestaurant(RestaurantUpdateRequest restaurantUpdateRequest) {

        log.info("Updating restaurant with id {}", restaurantUpdateRequest.getRestaurantId());

        Restaurant restaurant = findRestaurant(restaurantUpdateRequest.getRestaurantId());

        if(restaurantUpdateRequest.getLocation() != null)
            restaurant.setLocation(restaurantUpdateRequest.getLocation());
        if(restaurantUpdateRequest.getName() != null)
            restaurant.setName(restaurantUpdateRequest.getName());
        if(restaurantUpdateRequest.getDescription() != null)
            restaurant.setDescription(restaurantUpdateRequest.getDescription());

        return save(restaurant);
    }

    @Override
    @CachePut(value = "restaurant", key = "#restaurantReservHoursUpdateRequest.restaurantId")
    public Restaurant updateRestaurantReservationHours(RestaurantReservHoursUpdateRequest restaurantReservHoursUpdateRequest) {

        log.info("Updating restaurant with id {}", restaurantReservHoursUpdateRequest.getRestaurantId());

        Restaurant restaurant = findRestaurant(restaurantReservHoursUpdateRequest.getRestaurantId());
        restaurant.setReservationHours(
                getReservationHours(restaurantReservHoursUpdateRequest.getOpenTime(), restaurantReservHoursUpdateRequest.getCloseTime(), restaurantReservHoursUpdateRequest.getIntervalMinutes()));
        return save(restaurant);
    }

    @Override
    @CachePut(value = "restaurant", key = "#restaurant.id")
    public Restaurant changeRestaurantStatus(Restaurant restaurant) {

        log.info("Changing restaurant's with id {} status", restaurant.getId());

        if(restaurant.getStatus() != RestaurantStatus.PENDING)
            userProxy.updateUserStatus(jwtUtils.getAuthorizationHeader(), restaurant.getRestaurantAdminEmail());
        restaurant.setStatus(restaurant.getStatus().nextStatus());
        return save(restaurant);
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

    @Override
    @CachePut(value = "restaurant", key = "#restaurant.id")
    public Restaurant addCategory(Restaurant restaurant, Category category) {
        log.info("Adding category {} to restaurant {}", category.getName(), restaurant.getName());
        restaurant.getCategories().add(category);
        return save(restaurant);
    }

    @Override
    @CachePut(value = "restaurant", key = "#restaurant.id")
    public Restaurant removeCategory(Restaurant restaurant, Category category) {
        log.info("Removing category {} from restaurant {}", category.getName(), restaurant.getName());
        restaurant.getCategories().remove(category);
        return save(restaurant);
    }

    @Override
    public List<Restaurant> searchRestaurants(SearchRestaurantsRequest searchRestaurantsRequest) {

        log.info("Looking for restaurants");

        List<Restaurant> restaurants;
        if ((searchRestaurantsRequest.getCategories() == null || searchRestaurantsRequest.getCategories().isEmpty()) && searchRestaurantsRequest.getLocation() != null)
            restaurants = restaurantRepository.findAllByLocationIgnoreCase(searchRestaurantsRequest.getLocation());
        else if (searchRestaurantsRequest.getLocation() != null && !searchRestaurantsRequest.getLocation().isEmpty())
            restaurants = restaurantRepository.findAllByLocationIgnoreCaseAndCategoriesIn(searchRestaurantsRequest.getLocation(), searchRestaurantsRequest.getCategories());
        else if(searchRestaurantsRequest.getCategories() != null || !searchRestaurantsRequest.getCategories().isEmpty())
            restaurants = restaurantRepository.findAllByCategoriesIn(searchRestaurantsRequest.getCategories());
        else
            restaurants = restaurantRepository.findAll();

        return restaurants;
    }

    @Override
    @CachePut(value = "restaurant", key = "#restaurant.id")
    public Restaurant saveRestaurantLogo(Restaurant restaurant, MultipartFile logo) {

        log.info("Saving logo of restaurant {}", restaurant.getName());

        if(!List.of("jpg", "png", "jpeg").contains(FilenameUtils.getExtension(logo.getOriginalFilename()))) {
            throw new InvalidImageTypeException(logo.getOriginalFilename());
        }
        try {
            restaurant.setLogo(logo.getBytes());
        }
        catch (IOException exception){
            throw new InvalidImageException(logo.getOriginalFilename());
        }
        return save(restaurant);
    }

    @Override
    public void updateRestaurantRating(AverageRatingUpdateRequest ratingUpdateRequest) {

        log.info("Updating restaurant's {} average rating to {}", ratingUpdateRequest.getRestaurantId(), ratingUpdateRequest.getRating());

        Restaurant restaurant = findRestaurant(ratingUpdateRequest.getRestaurantId());
        restaurant.setAverageRating(ratingUpdateRequest.getRating());
        restaurant.setNumRatings(ratingUpdateRequest.getNumRatings());
        save(restaurant);
    }
}
