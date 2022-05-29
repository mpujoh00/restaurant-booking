package com.restaurant.booking.booking.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ReservationCreationRequest {

    @NotEmpty
    private String userId;
    @NotEmpty
    private String restaurantId;
    @NotNull
    private Integer numPeople;
    @NotNull
    private LocalDate date;
    @NotNull
    private LocalTime time;
}
