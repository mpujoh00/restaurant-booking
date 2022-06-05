package com.restaurant.booking.booking.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
@Document(collection = "reservations")
public class Reservation {

    @Id
    private String id;
    private String userId;
    private String restaurantId;
    private Integer numPeople;
    private ReservationStatus status;
    @DBRef
    private ReservationSlot reservationSlot;
    @Transient
    private String userName;
    @Transient
    private String restaurantName;

    public Reservation(String userId, String restaurantId, Integer numPeople, ReservationSlot reservationSlot) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.numPeople = numPeople;
        this.reservationSlot = reservationSlot;
        this.status = ReservationStatus.PENDING;
    }
}
