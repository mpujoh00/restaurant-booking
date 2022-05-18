package com.restaurant.booking.restaurant.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class RestaurantRegistrationRequest {

    @NotEmpty
    private String name;
    @NotEmpty
    private String location;
    @NotEmpty
    @Size(min = 10)
    private String restaurantAdminEmail;
    @NotNull
    private LocalTime openTime;
    @NotNull
    private LocalTime closeTime;
    @NotNull
    private Interval intervalMinutes;
}
