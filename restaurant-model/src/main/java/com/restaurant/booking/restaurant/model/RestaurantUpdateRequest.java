package com.restaurant.booking.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantUpdateRequest {

    @NotEmpty
    @Size(min = 10)
    private String restaurantAdminEmail;
    private String name;
    private String location;
}
