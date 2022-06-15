package com.restaurant.booking.restaurant.model;

import lombok.*;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
@Document(collection = "restaurants")
public class Restaurant {

    @Id
    private String id;

    private String name;
    private String location;
    private String address;
    private String openingTime;
    private String closingTime;
    private List<LocalTime> reservationHours;
    private RestaurantStatus status;
    private String restaurantAdminEmail;
    private Binary logo;
    private String description;
    private String menu;

    @DBRef
    private Set<Category> categories;

    public Restaurant(RestaurantRegistrationRequest restaurantRegistrationRequest) {

        this.name = restaurantRegistrationRequest.getName();
        this.location = restaurantRegistrationRequest.getLocation();
        this.address = restaurantRegistrationRequest.getAddress();
        this.openingTime = restaurantRegistrationRequest.getOpenTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.closingTime = restaurantRegistrationRequest.getCloseTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.status = RestaurantStatus.PENDING;
        this.categories = new HashSet<>();
    }
}
