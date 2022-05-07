package com.restaurant.booking.user.model;

public enum UserStatus {

    ENABLED,
    DISABLED;

    public UserStatus getNextStatus(){
        if(this == ENABLED)
            return DISABLED;
        else
            return ENABLED;
    }
}
