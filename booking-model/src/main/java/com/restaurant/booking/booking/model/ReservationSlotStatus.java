package com.restaurant.booking.booking.model;

public enum ReservationSlotStatus {

    FREE,
    BOOKED,
    ONE_WAY_BOOKED,
    TWO_WAY_BOOKED;

    public ReservationSlotStatus getNextStatus(){
        if(this == FREE) {
            return  BOOKED;
        } else {
            return FREE;
        }
    }

    public ReservationSlotStatus getNextStatusByParent(ReservationSlotStatus parentStatus){
        if(this == FREE && parentStatus == FREE)
            return ONE_WAY_BOOKED;
        else if(this == ONE_WAY_BOOKED && parentStatus == BOOKED)
            return FREE;
        else if(this == ONE_WAY_BOOKED && parentStatus == FREE)
            return TWO_WAY_BOOKED;
        else if(this==TWO_WAY_BOOKED && parentStatus == BOOKED)
            return ONE_WAY_BOOKED;
        else
            return FREE;
    }
}

