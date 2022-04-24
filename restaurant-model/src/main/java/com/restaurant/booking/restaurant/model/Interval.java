package com.restaurant.booking.restaurant.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Interval {
    FIFTEEN_MIN(15),
    THIRTY_MIN(30),
    FORTYFIVE_MIN(45),
    SIXTY_MIN(60);

    private int minutes;

    Interval(int minutes) {
        this.minutes = minutes;
    }

    public int getMinutes(){
        return this.minutes;
    }

    @JsonCreator
    public static Interval getIntervalFromMinutes(int minutes){
        for(Interval interval: Interval.values()){
            if(interval.getMinutes() == minutes)
                return interval;
        }
        return null;
    }
}
