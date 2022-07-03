package com.restaurant.booking.booking.model;

import java.util.List;

public enum ReservationStatus {

    PENDING,
    CONFIRMED,
    ATTENDED,
    CANCELED;

    public static List<ReservationStatus> getValidStatus(ReservationStatus status){
        if(status == PENDING)
            return List.of(CONFIRMED, CANCELED);
        else if(status == CONFIRMED)
            return List.of(ATTENDED, CANCELED);
        else if(status == CANCELED)
            return List.of(CANCELED);
        else
            return List.of(ATTENDED);
    }
}
