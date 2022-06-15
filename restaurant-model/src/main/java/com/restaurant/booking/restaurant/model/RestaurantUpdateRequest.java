package com.restaurant.booking.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantUpdateRequest {

    @NotEmpty
    private String restaurantId;
    @NotEmpty
    private String name;
    @NotEmpty
    private String location;
    @NotEmpty
    private String address;
    private String description;
    private String menu;
}
