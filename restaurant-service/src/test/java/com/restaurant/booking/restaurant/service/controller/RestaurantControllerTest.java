package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.*;
import com.restaurant.booking.restaurant.service.service.CategoryServiceImpl;
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
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

    @Mock
    private RestaurantServiceImpl restaurantService;

    @Mock
    private CategoryServiceImpl categoryService;

    @InjectMocks
    private RestaurantControllerImpl restaurantController;

    @Test
    void register(){
        RestaurantRegistrationRequest request = RestaurantRegistrationRequest.builder().build();
        Restaurant restaurant = Restaurant.builder().build();

        Mockito.when(restaurantService.register(request)).thenReturn(restaurant);

        ResponseEntity<Restaurant> responseEntity = restaurantController.register(request);

        Mockito.verify(restaurantService).register(request);
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertEquals(restaurant, responseEntity.getBody());
    }

    @Test
    void getRestaurant(){
        Restaurant restaurant = Restaurant.builder().id("id").build();

        Mockito.when(restaurantService.findRestaurant("id")).thenReturn(restaurant);

        ResponseEntity<Restaurant> responseEntity = restaurantController.getRestaurant("id");

        Mockito.verify(restaurantService).findRestaurant("id");
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(restaurant, responseEntity.getBody());
    }

    @Test
    void getEnabledRestaurants(){
        List<Restaurant> restaurants = List.of(Restaurant.builder().id("id").build(), Restaurant.builder().id("id2").build());

        Mockito.when(restaurantService.findRestaurantsByStatus(RestaurantStatus.ENABLED)).thenReturn(restaurants);

        ResponseEntity<List<Restaurant>> responseEntity = restaurantController.getEnabledRestaurants();

        Mockito.verify(restaurantService).findRestaurantsByStatus(RestaurantStatus.ENABLED);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(restaurants, responseEntity.getBody());
    }

    @Test
    void getRestaurantReservationHours() {
        List<LocalTime> reservationHours = List.of(LocalTime.now());
        Restaurant restaurant = Restaurant.builder().reservationHours(reservationHours).build();

        Mockito.when(restaurantService.findRestaurant("id")).thenReturn(restaurant);

        ResponseEntity<List<LocalTime>> responseEntity = restaurantController.getRestaurantsReservationHours("id");

        Mockito.verify(restaurantService).findRestaurant("id");
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(reservationHours, responseEntity.getBody());
    }

    @Test
    void deleteRestaurant(){
        ResponseEntity<Void> responseEntity = restaurantController.deleteRestaurant("id");

        Mockito.verify(restaurantService).deleteRestaurant("id");
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void updateRestaurant(){
        RestaurantUpdateRequest request = new RestaurantUpdateRequest("id", "restaurant", "leon", "direccion", null);
        Restaurant restaurant = Restaurant.builder().id("id").build();

        Mockito.when(restaurantService.updateRestaurant(request)).thenReturn(restaurant);

        ResponseEntity<Restaurant> responseEntity = restaurantController.updateRestaurant(request);

        Mockito.verify(restaurantService).updateRestaurant(request);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(restaurant, responseEntity.getBody());
    }

    @Test
    void updateRestaurantReservationHours(){
        RestaurantReservHoursUpdateRequest request = new RestaurantReservHoursUpdateRequest("id", LocalTime.of(9, 0),
                LocalTime.of(10, 0), Interval.getIntervalFromMinutes(30));
        Restaurant restaurant = Restaurant.builder().id("id").build();

        Mockito.when(restaurantService.updateRestaurantReservationHours(request)).thenReturn(restaurant);

        ResponseEntity<Restaurant> responseEntity = restaurantController.updateRestaurantReservationHours(request);

        Mockito.verify(restaurantService).updateRestaurantReservationHours(request);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(restaurant, responseEntity.getBody());
    }

    @Test
    void addCategory(){
        Restaurant restaurant = Restaurant.builder().id("id").build();
        Category category = new Category("category");

        Mockito.when(restaurantService.findRestaurant("id")).thenReturn(restaurant);
        Mockito.when(categoryService.getCategory("category")).thenReturn(category);
        Mockito.when(restaurantService.addCategory(restaurant, category)).thenReturn(restaurant);

        ResponseEntity<Restaurant> responseEntity = restaurantController.addCategory("id", "category");

        Mockito.verify(restaurantService).findRestaurant("id");
        Mockito.verify(categoryService).getCategory("category");
        Mockito.verify(restaurantService).addCategory(restaurant, category);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(restaurant, responseEntity.getBody());
    }

    @Test
    void removeCategory(){
        Restaurant restaurant = Restaurant.builder().id("id").build();
        Category category = new Category("category");

        Mockito.when(restaurantService.findRestaurant("id")).thenReturn(restaurant);
        Mockito.when(categoryService.getCategory("category")).thenReturn(category);
        Mockito.when(restaurantService.removeCategory(restaurant, category)).thenReturn(restaurant);

        ResponseEntity<Restaurant> responseEntity = restaurantController.removeCategory("id", "category");

        Mockito.verify(restaurantService).findRestaurant("id");
        Mockito.verify(categoryService).getCategory("category");
        Mockito.verify(restaurantService).removeCategory(restaurant, category);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(restaurant, responseEntity.getBody());
    }

    @Test
    void searchRestaurants(){
        SearchRestaurantsRequest request = new SearchRestaurantsRequest("city", null);
        List<Restaurant> restaurants = List.of(Restaurant.builder().build());

        Mockito.when(restaurantService.searchRestaurants(request)).thenReturn(restaurants);

        ResponseEntity<List<Restaurant>> responseEntity = restaurantController.searchRestaurants(request);

        Mockito.verify(restaurantService).searchRestaurants(request);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(restaurants, responseEntity.getBody());
    }

    @Test
    void saveLogo(){
        Restaurant restaurant = Restaurant.builder().build();
        MockMultipartFile logo = new MockMultipartFile("file", "".getBytes());

        Mockito.when(restaurantService.findRestaurant("id")).thenReturn(restaurant);
        Mockito.when(restaurantService.saveRestaurantLogo(restaurant, logo)).thenReturn(restaurant);

        ResponseEntity<Restaurant> responseEntity = restaurantController.saveLogo("id", logo);

        Mockito.verify(restaurantService).findRestaurant("id");
        Mockito.verify(restaurantService).saveRestaurantLogo(restaurant, logo);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(restaurant, responseEntity.getBody());
    }
}
