package com.restaurant.booking.booking.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RatingRequestCreation {

    @NotNull
    private Integer value;
    private String comment;
    @NotEmpty
    private String reservationId;
    @NotEmpty
    private String restaurantId;
    @NotEmpty
    private String userId;
    @NotEmpty
    private String userName;
}
