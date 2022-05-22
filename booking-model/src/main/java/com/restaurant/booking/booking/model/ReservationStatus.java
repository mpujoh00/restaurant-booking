package com.restaurant.booking.booking.model;

import java.util.List;

public enum ReservationStatus {

    PENDING,
    CONFIRMED,
    CANCELED;

    public static List<ReservationStatus> getValidStatus(ReservationStatus status){
        if(status == PENDING)
            return List.of(CONFIRMED, CANCELED);
        else
            return List.of(CANCELED);
    }
}
