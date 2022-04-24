package com.restaurant.booking.restaurant.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RestaurantRegistrationRequest {

    @NotEmpty
    private String name;
    @NotEmpty
    private String location;
    @NotEmpty
    private String restaurantAdminEmail;
    @NotNull
    private LocalTime openTime;
    @NotNull
    private LocalTime closeTime;
    @NotNull
    private Interval intervalMinutes;
}
