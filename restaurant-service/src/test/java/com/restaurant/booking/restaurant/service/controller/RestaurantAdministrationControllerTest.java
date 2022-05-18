package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.Restaurant;
import com.restaurant.booking.restaurant.model.RestaurantStatus;
import com.restaurant.booking.restaurant.service.service.RestaurantServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class RestaurantAdministrationControllerTest {

    @Mock
    private RestaurantServiceImpl restaurantService;

    @InjectMocks
    private RestaurantAdministrationControllerImpl restaurantAdministrationController;

    @Test
    void getAllRestaurants(){
        List<Restaurant> restaurants = List.of(Restaurant.builder().id("id").build(), Restaurant.builder().id("id2").build());

        Mockito.when(restaurantService.findAllRestaurants()).thenReturn(restaurants);

        ResponseEntity<List<Restaurant>> responseEntity = restaurantAdministrationController.getAllRestaurants();

        Mockito.verify(restaurantService).findAllRestaurants();
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(restaurants, responseEntity.getBody());
    }

    @Test
    void getDisabledRestaurants(){
        List<Restaurant> restaurants = List.of(Restaurant.builder().id("id").build(), Restaurant.builder().id("id2").build());

        Mockito.when(restaurantService.findRestaurantsByStatus(RestaurantStatus.DISABLED)).thenReturn(restaurants);

        ResponseEntity<List<Restaurant>> responseEntity = restaurantAdministrationController.getDisabledRestaurants();

        Mockito.verify(restaurantService).findRestaurantsByStatus(RestaurantStatus.DISABLED);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(restaurants, responseEntity.getBody());
    }

    @Test
    void getPendingRestaurants(){
        List<Restaurant> restaurants = List.of(Restaurant.builder().id("id").build(), Restaurant.builder().id("id2").build());

        Mockito.when(restaurantService.findRestaurantsByStatus(RestaurantStatus.PENDING)).thenReturn(restaurants);

        ResponseEntity<List<Restaurant>> responseEntity = restaurantAdministrationController.getPendingRestaurants();

        Mockito.verify(restaurantService).findRestaurantsByStatus(RestaurantStatus.PENDING);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(restaurants, responseEntity.getBody());
    }

    @Test
    void updateRestaurantStatus(){
        Restaurant restaurant = Restaurant.builder().id("id").build();

        Mockito.when(restaurantService.findRestaurant("id")).thenReturn(restaurant);
        Mockito.when(restaurantService.changeRestaurantStatus(restaurant)).thenReturn(restaurant);

        ResponseEntity<Restaurant> responseEntity = restaurantAdministrationController.updateRestaurantStatus("id");

        Mockito.verify(restaurantService).findRestaurant("id");
        Mockito.verify(restaurantService).changeRestaurantStatus(restaurant);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(restaurant, responseEntity.getBody());
    }

}
