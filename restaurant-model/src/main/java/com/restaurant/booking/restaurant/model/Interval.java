package com.restaurant.booking.restaurant.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Interval {
    @JsonProperty("15")
    FIFTEEN_MIN(15),
    @JsonProperty("30")
    THIRTY_MIN(30),
    @JsonProperty("45")
    FORTYFIVE_MIN(45),
    @JsonProperty("60")
    SIXTY_MIN(60);

    private int minutes;

    Interval(int minutes) {
        this.minutes = minutes;
    }

    public int getMinutes(){
        return this.minutes;
    }

    public static Interval getIntervalFromMinutes(int minutes){
        for(Interval interval: Interval.values()){
            if(interval.getMinutes() == minutes)
                return interval;
        }
        return null;
    }
}
