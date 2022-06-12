package com.restaurant.booking.restaurant.model;

import lombok.*;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
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
    private List<LocalTime> reservationHours;
    private RestaurantStatus status;
    private String restaurantAdminEmail;
    private Binary logo;

    @DBRef
    private Set<Category> categories;

    public Restaurant(RestaurantRegistrationRequest restaurantRegistrationRequest) {

        this.name = restaurantRegistrationRequest.getName();
        this.location = restaurantRegistrationRequest.getLocation();
        this.status = RestaurantStatus.PENDING;
        this.categories = new HashSet<>();
    }
}
