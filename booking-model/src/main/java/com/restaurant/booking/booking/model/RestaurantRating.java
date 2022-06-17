package com.restaurant.booking.booking.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class RestaurantRating {

    private Double averageRating;
    private Integer numRatings;
}
