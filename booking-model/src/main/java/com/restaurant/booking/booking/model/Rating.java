package com.restaurant.booking.booking.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
@Document(collection = "ratings")
public class Rating {

    @Id
    private String id;
    private Double value;
    private String comment;
    private String reservationId;
    private String restaurantId;
    private String userId;
    private String userName;
    private LocalDate date;
    private RatingStatus ratingStatus;

    public Rating(RatingCreationRequest requestCreation){
        this.value = requestCreation.getValue();
        if(requestCreation.getComment() != null && !requestCreation.getComment().isEmpty())
            this.comment = requestCreation.getComment();
        this.reservationId = requestCreation.getReservationId();
        this.restaurantId = requestCreation.getRestaurantId();
        this.userId = requestCreation.getUserId();
        this.userName = requestCreation.getUserName();
        this.date = LocalDate.now();
        this.ratingStatus = RatingStatus.OK;
    }
}