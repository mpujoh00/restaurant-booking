package com.restaurant.booking.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantReservHoursUpdateRequest {

    @NotEmpty
    @Size(min = 10)
    private String restaurantId;
    @NotNull
    private LocalTime openTime;
    @NotNull
    private LocalTime closeTime;
    @NotNull
    private Interval intervalMinutes;
}
