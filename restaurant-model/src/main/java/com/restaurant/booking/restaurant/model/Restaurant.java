package com.restaurant.booking.restaurant.model;

import lombok.*;
import org.bson.BsonBinarySubType;
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
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Binary logo;
    private String description;
    private String menu;
    private Double averageRating;
    private Integer numRatings;

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
        this.averageRating = 0.0;
        this.numRatings = 0;
    }

    public byte[] getLogo(){
        return this.logo != null ? this.logo.getData() : null;
    }

    public void setLogo(byte[] logo){
        this.logo = new Binary(BsonBinarySubType.BINARY, logo);
    }
}
