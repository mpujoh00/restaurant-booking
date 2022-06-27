package com.restaurant.booking.restaurant.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AverageRatingUpdateRequest {

    private String restaurantId;
    private Double rating;
    private Integer numRatings;
}
