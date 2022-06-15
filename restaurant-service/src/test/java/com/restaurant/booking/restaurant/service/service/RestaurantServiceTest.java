package com.restaurant.booking.restaurant.service.service;

import com.restaurant.booking.feign.client.UserProxy;
import com.restaurant.booking.feign.client.exception.BadRequestException;
import com.restaurant.booking.jwt.utils.JwtUtils;
import com.restaurant.booking.restaurant.model.*;
import com.restaurant.booking.restaurant.service.exception.InvalidImageTypeException;
import com.restaurant.booking.restaurant.service.exception.RestaurantAlreadyExistsException;
import com.restaurant.booking.restaurant.service.exception.RestaurantNotFoundException;
import com.restaurant.booking.restaurant.service.exception.UserNotFoundException;
import com.restaurant.booking.restaurant.service.repository.RestaurantRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserProxy userProxy;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @Test
    void register() {
        RestaurantRegistrationRequest request = new RestaurantRegistrationRequest("restaurant", "leon", "direccion", "chef@gmail.com",
                LocalTime.of(9, 0), LocalTime.of(10, 0), Interval.getIntervalFromMinutes(30));
        List<LocalTime> reservationHours = List.of(LocalTime.of(9, 0), LocalTime.of(9, 30));
        Restaurant restaurant = new Restaurant(request);
        restaurant.setRestaurantAdminEmail("chef@gmail.com");
        restaurant.setReservationHours(reservationHours);

        Mockito.when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        Mockito.when(jwtUtils.getAuthorizationHeader()).thenReturn("HEADER");

        Restaurant obtainedRestaurant = restaurantService.register(request);

        Mockito.verify(restaurantRepository).save(restaurant);
        Assertions.assertEquals(restaurant, obtainedRestaurant);
    }

    @Test
    void register_restaurantAlreadyExists() {
        RestaurantRegistrationRequest request = new RestaurantRegistrationRequest("restaurant", "leon", "direccion", "chef@gmail.com",
                LocalTime.of(9, 0), LocalTime.of(10, 0), Interval.getIntervalFromMinutes(30));
        List<LocalTime> reservationHours = List.of(LocalTime.of(9, 0), LocalTime.of(9, 30));
        Restaurant restaurant = new Restaurant(request);
        restaurant.setRestaurantAdminEmail("chef@gmail.com");
        restaurant.setReservationHours(reservationHours);

        Mockito.when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        Mockito.when(jwtUtils.getAuthorizationHeader()).thenReturn("HEADER");
        doThrow(new BadRequestException()).when(userProxy).addRestaurant("HEADER", "chef@gmail.com", null);

        RestaurantAlreadyExistsException exception = Assertions.assertThrows(
                RestaurantAlreadyExistsException.class, () -> restaurantService.register(request));
        Assertions.assertEquals("User with email chef@gmail.com already has a restaurant", exception.getMessage());

        Mockito.verify(restaurantRepository).save(restaurant);
    }

    @Test
    void register_userNotFound() {
        RestaurantRegistrationRequest request = new RestaurantRegistrationRequest("restaurant", "leon", "direccion", "chef@gmail.com",
                LocalTime.of(9, 0), LocalTime.of(10, 0), Interval.getIntervalFromMinutes(30));
        List<LocalTime> reservationHours = List.of(LocalTime.of(9, 0), LocalTime.of(9, 30));
        Restaurant restaurant = new Restaurant(request);
        restaurant.setRestaurantAdminEmail("chef@gmail.com");
        restaurant.setReservationHours(reservationHours);

        Mockito.when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        Mockito.when(jwtUtils.getAuthorizationHeader()).thenReturn("HEADER");
        doThrow(new UserNotFoundException("chef@gmail.com")).when(userProxy).addRestaurant("HEADER", "chef@gmail.com", null);

        UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class, () -> restaurantService.register(request));
        Assertions.assertEquals("User with email: chef@gmail.com not found", exception.getMessage());

        Mockito.verify(restaurantRepository).save(restaurant);
    }

    @Test
    void findRestaurant() {
        Restaurant restaurant = Restaurant.builder().id("id").build();

        Mockito.when(restaurantRepository.findById("id")).thenReturn(Optional.of(restaurant));

        Restaurant obtainedRestaurant = restaurantService.findRestaurant("id");

        Mockito.verify(restaurantRepository).findById("id");
        Assertions.assertEquals(restaurant, obtainedRestaurant);
    }

    @Test
    void findRestaurant_restaurantNotFound() {
        Mockito.when(restaurantRepository.findById("id")).thenReturn(Optional.empty());

        RestaurantNotFoundException exception = Assertions.assertThrows(
                RestaurantNotFoundException.class, () -> restaurantService.findRestaurant("id"));
        Assertions.assertEquals("Restaurant with id id not found", exception.getMessage());

        Mockito.verify(restaurantRepository).findById("id");
    }

    @Test
    void findAllRestaurants() {
        List<Restaurant> restaurants = List.of(Restaurant.builder().id("id").build(), Restaurant.builder().id("id2").build());

        Mockito.when(restaurantRepository.findAll()).thenReturn(restaurants);

        List<Restaurant> obtainedRestaurants = restaurantService.findAllRestaurants();

        Mockito.verify(restaurantRepository).findAll();
        Assertions.assertEquals(restaurants, obtainedRestaurants);
    }

    @Test
    void findRestaurantsByStatus() {
        List<Restaurant> restaurants = List.of(Restaurant.builder().id("id").build(), Restaurant.builder().id("id2").build());

        Mockito.when(restaurantRepository.findByStatus(RestaurantStatus.DISABLED)).thenReturn(restaurants);

        List<Restaurant> obtainedRestaurants = restaurantService.findRestaurantsByStatus(RestaurantStatus.DISABLED);

        Mockito.verify(restaurantRepository).findByStatus(RestaurantStatus.DISABLED);
        Assertions.assertEquals(restaurants, obtainedRestaurants);
    }

    @Test
    void deleteRestaurant() {
        restaurantService.deleteRestaurant("id");

        Mockito.verify(restaurantRepository).deleteById("id");
    }

    @Test
    void updateRestaurant() {
        RestaurantUpdateRequest request = new RestaurantUpdateRequest("id", "restaurant", "leon", "direccion", null, null);
        Restaurant restaurant = Restaurant.builder().id("id").name("restaurant").location("leon").build();

        Mockito.when(restaurantRepository.findById("id")).thenReturn(Optional.of(Restaurant.builder().id("id").build()));
        Mockito.when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        Restaurant obtainedRestaurant = restaurantService.updateRestaurant(request);

        Mockito.verify(restaurantRepository).findById("id");
        Mockito.verify(restaurantRepository).save(restaurant);
        Assertions.assertEquals(restaurant, obtainedRestaurant);
    }

    @Test
    void updateRestaurantReservationHours() {
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
    void changeRestaurantStatus() {
        Restaurant restaurant = Restaurant.builder().id("id").status(RestaurantStatus.ENABLED).build();

        Mockito.when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        Restaurant obtainedRestaurant = restaurantService.changeRestaurantStatus(Restaurant.builder().id("id").status(RestaurantStatus.PENDING).build());

        Mockito.verify(restaurantRepository).save(restaurant);
        Assertions.assertEquals(restaurant, obtainedRestaurant);
    }

    @Test
    void addCategory(){
        Restaurant restaurant = Restaurant.builder().categories(new HashSet<>()).build();
        Category category = new Category("category");
        Restaurant restaurantAfter = Restaurant.builder().categories(new HashSet<>()).build();
        restaurantAfter.getCategories().add(category);

        Mockito.when(restaurantRepository.save(restaurantAfter)).thenReturn(restaurantAfter);

        Restaurant obtainedRestaurant = restaurantService.addCategory(restaurant, category);

        Mockito.verify(restaurantRepository).save(restaurantAfter);
        Assertions.assertEquals(restaurantAfter, obtainedRestaurant);
    }

    @Test
    void removeCategory(){
        Restaurant restaurant = Restaurant.builder().categories(new HashSet<>()).build();
        Category category = new Category("category");
        Restaurant restaurantBefore = Restaurant.builder().categories(new HashSet<>()).build();
        restaurantBefore.getCategories().add(category);

        Mockito.when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        Restaurant obtainedRestaurant = restaurantService.removeCategory(restaurantBefore, category);

        Mockito.verify(restaurantRepository).save(restaurant);
        Assertions.assertEquals(restaurant, obtainedRestaurant);
    }

    @Test
    void searchRestaurants_location(){
        SearchRestaurantsRequest request = new SearchRestaurantsRequest("city", null);

        Mockito.when(restaurantRepository.findAllByLocationIgnoreCase("city")).thenReturn(List.of(Restaurant.builder().build()));

        List<Restaurant> restaurants = restaurantService.searchRestaurants(request);

        Mockito.verify(restaurantRepository).findAllByLocationIgnoreCase("city");
        Assertions.assertEquals(1, restaurants.size());
    }

    @Test
    void searchRestaurants_locationAndCategories(){
        List<Category> categories = List.of(new Category("category"));
        SearchRestaurantsRequest request = new SearchRestaurantsRequest("city", categories);

        Mockito.when(restaurantRepository.findAllByLocationIgnoreCaseAndCategoriesIn("city", categories)).thenReturn(List.of(Restaurant.builder().build()));

        List<Restaurant> restaurants = restaurantService.searchRestaurants(request);

        Mockito.verify(restaurantRepository).findAllByLocationIgnoreCaseAndCategoriesIn("city", categories);
        Assertions.assertEquals(1, restaurants.size());
    }

    @Test
    void saveRestaurantLogo() throws IOException {
        Restaurant restaurant = Restaurant.builder().build();
        MockMultipartFile file = new MockMultipartFile("file", "file.jpg", "", "".getBytes());
        Binary logo = new Binary(BsonBinarySubType.BINARY, file.getBytes());
        Restaurant restaurantAfter = Restaurant.builder().logo(logo).build();

        Mockito.when(restaurantRepository.save(restaurantAfter)).thenReturn(restaurantAfter);

        Restaurant obtainedRestaurant = restaurantService.saveRestaurantLogo(restaurant, file);

        Mockito.verify(restaurantRepository).save(restaurantAfter);
        Assertions.assertEquals(restaurantAfter, obtainedRestaurant);
    }

    @Test
    void saveRestaurantLogo_invalidType() {
        Restaurant restaurant = Restaurant.builder().build();
        MockMultipartFile file = new MockMultipartFile("file", "file.gif", "", "".getBytes());

        InvalidImageTypeException exception = Assertions.assertThrows(
                InvalidImageTypeException.class, () -> restaurantService.saveRestaurantLogo(restaurant, file));
        Assertions.assertEquals("Couldn't upload image file.gif, valid extensions are: *.jpg, *.png, *.jpeg", exception.getMessage());
    }
}
