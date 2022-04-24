package com.restaurant.booking.restaurant.model;

import com.restaurant.booking.user.model.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.List;

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

    @DBRef
    private User restaurantAdmin;

    public Restaurant(RestaurantRegistrationRequest restaurantRegistrationRequest) {

        this.name = restaurantRegistrationRequest.getName();
        this.location = restaurantRegistrationRequest.getLocation();
        this.status = RestaurantStatus.PENDING;
    }
}
