package com.restaurant.booking.restaurant.service.service;

import com.restaurant.booking.feign.client.UserProxy;
import com.restaurant.booking.feign.client.exception.BadRequestException;
import com.restaurant.booking.feign.client.exception.NotFoundException;
import com.restaurant.booking.restaurant.model.*;
import com.restaurant.booking.restaurant.service.exception.RestaurantAlreadyExistsException;
import com.restaurant.booking.restaurant.service.exception.RestaurantNotFoundException;
import com.restaurant.booking.restaurant.service.exception.UserNotFoundException;
import com.restaurant.booking.restaurant.service.repository.RestaurantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserProxy userProxy;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @Test
    void register(){
        RestaurantRegistrationRequest request = new RestaurantRegistrationRequest("restaurant", "leon", "chef@gmail.com",
                LocalTime.of(9, 0), LocalTime.of(10, 0), Interval.getIntervalFromMinutes(30));
        List<LocalTime> reservationHours = List.of(LocalTime.of(9, 0), LocalTime.of(9, 30));
        Restaurant restaurant = new Restaurant(request);
        restaurant.setReservationHours(reservationHours);

        Mockito.when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        Restaurant obtainedRestaurant = restaurantService.register(request, null);

        Mockito.verify(restaurantRepository).save(restaurant);
        Assertions.assertEquals(restaurant, obtainedRestaurant);
    }

    @Test
    void register_restaurantAlreadyExists(){
        RestaurantRegistrationRequest request = new RestaurantRegistrationRequest("restaurant", "leon", "chef@gmail.com",
                LocalTime.of(9, 0), LocalTime.of(10, 0), Interval.getIntervalFromMinutes(30));
        List<LocalTime> reservationHours = List.of(LocalTime.of(9, 0), LocalTime.of(9, 30));
        Restaurant restaurant = new Restaurant(request);
        restaurant.setReservationHours(reservationHours);

        Mockito.when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        doThrow(new BadRequestException()).when(userProxy).addRestaurant(null, "chef@gmail.com", null);

        RestaurantAlreadyExistsException exception = Assertions.assertThrows(
                RestaurantAlreadyExistsException.class, () -> restaurantService.register(request, null));
        Assertions.assertEquals("User with email chef@gmail.com already has a restaurant", exception.getMessage());

        Mockito.verify(restaurantRepository).save(restaurant);
    }

    @Test
    void register_userNotFound(){
        RestaurantRegistrationRequest request = new RestaurantRegistrationRequest("restaurant", "leon", "chef@gmail.com",
                LocalTime.of(9, 0), LocalTime.of(10, 0), Interval.getIntervalFromMinutes(30));
        List<LocalTime> reservationHours = List.of(LocalTime.of(9, 0), LocalTime.of(9, 30));
        Restaurant restaurant = new Restaurant(request);
        restaurant.setReservationHours(reservationHours);

        Mockito.when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        doThrow(new NotFoundException()).when(userProxy).addRestaurant(null, "chef@gmail.com", null);

        UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class, () -> restaurantService.register(request, null));
        Assertions.assertEquals("User with email: chef@gmail.com not found", exception.getMessage());

        Mockito.verify(restaurantRepository).save(restaurant);
    }

    @Test
    void findRestaurant(){
        Restaurant restaurant = Restaurant.builder().id("id").build();

        Mockito.when(restaurantRepository.findById("id")).thenReturn(Optional.of(restaurant));

        Restaurant obtainedRestaurant = restaurantService.findRestaurant("id");

        Mockito.verify(restaurantRepository).findById("id");
        Assertions.assertEquals(restaurant, obtainedRestaurant);
    }

    @Test
    void findRestaurant_restaurantNotFound(){
        Mockito.when(restaurantRepository.findById("id")).thenReturn(Optional.empty());

        RestaurantNotFoundException exception = Assertions.assertThrows(
                RestaurantNotFoundException.class, () -> restaurantService.findRestaurant("id"));
        Assertions.assertEquals("Restaurant with id id not found", exception.getMessage());

        Mockito.verify(restaurantRepository).findById("id");
    }

    @Test
    void findAllRestaurants(){
        List<Restaurant> restaurants = List.of(Restaurant.builder().id("id").build(), Restaurant.builder().id("id2").build());

        Mockito.when(restaurantRepository.findAll()).thenReturn(restaurants);

        List<Restaurant> obtainedRestaurants = restaurantService.findAllRestaurants();

        Mockito.verify(restaurantRepository).findAll();
        Assertions.assertEquals(restaurants, obtainedRestaurants);
    }

    @Test
    void findRestaurantsByStatus(){
        List<Restaurant> restaurants = List.of(Restaurant.builder().id("id").build(), Restaurant.builder().id("id2").build());

        Mockito.when(restaurantRepository.findByStatus(RestaurantStatus.DISABLED)).thenReturn(restaurants);

        List<Restaurant> obtainedRestaurants = restaurantService.findRestaurantsByStatus(RestaurantStatus.DISABLED);

        Mockito.verify(restaurantRepository).findByStatus(RestaurantStatus.DISABLED);
        Assertions.assertEquals(restaurants, obtainedRestaurants);
    }

    @Test
    void deleteRestaurant(){
        restaurantService.deleteRestaurant("id");

        Mockito.verify(restaurantRepository).deleteById("id");
    }

    @Test
    void updateRestaurant(){
        RestaurantUpdateRequest request = new RestaurantUpdateRequest("id", "restaurant", "leon");
        Restaurant restaurant = Restaurant.builder().id("id").name("restaurant").location("leon").build();

        Mockito.when(restaurantRepository.findById("id")).thenReturn(Optional.of(Restaurant.builder().id("id").build()));
        Mockito.when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        Restaurant obtainedRestaurant = restaurantService.updateRestaurant(request);

        Mockito.verify(restaurantRepository).findById("id");
        Mockito.verify(restaurantRepository).save(restaurant);
        Assertions.assertEquals(restaurant, obtainedRestaurant);
    }

    @Test
    void updateRestaurantReservationHours(){
        RestaurantReservHoursUpdateRequest request = new RestaurantReservHoursUpdateRequest("id", LocalTime.of(9, 0),
                LocalTime.of(10, 0), Interval.getIntervalFromMinutes(30));
        List<LocalTime> reservationHours = List.of(LocalTime.of(9, 0), LocalTime.of(9, 30));
        Restaurant restaurant = Restaurant.builder().id("id").name("restaurant").location("leon").build();
        restaurant.setReservationHours(reservationHours);

        Mockito.when(restaurantRepository.findById("id")).thenReturn(Optional.of(restaurant));
        Mockito.when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        Restaurant obtainedRestaurant = restaurantService.updateRestaurantReservationHours(request);

        Mockito.verify(restaurantRepository).findById("id");
        Mockito.verify(restaurantRepository).save(restaurant);
        Assertions.assertEquals(restaurant, obtainedRestaurant);
    }

    @Test
    void changeRestaurantStatus(){
        Restaurant restaurant = Restaurant.builder().id("id").status(RestaurantStatus.ENABLED).build();

        Mockito.when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        Restaurant obtainedRestaurant = restaurantService.changeRestaurantStatus(Restaurant.builder().id("id").status(RestaurantStatus.PENDING).build());

        Mockito.verify(restaurantRepository).save(restaurant);
        Assertions.assertEquals(restaurant, obtainedRestaurant);
    }
}
