package com.restaurant.booking.booking.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class RatingCreationRequest {

    @NotNull
    private Double value;
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
