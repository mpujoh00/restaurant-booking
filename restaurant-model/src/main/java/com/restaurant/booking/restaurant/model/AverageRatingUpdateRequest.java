package com.restaurant.booking.restaurant.model;

import lombok.*;

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
