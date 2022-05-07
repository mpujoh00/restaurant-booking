package com.restaurant.booking.restaurant.model;

public enum RestaurantStatus {
    PENDING{
        @Override
        public RestaurantStatus nextStatus() {
            return ENABLED;
        }
    },
    ENABLED {
        @Override
        public RestaurantStatus nextStatus() {
            return DISABLED;
        }
    },
    DISABLED {
        @Override
        public RestaurantStatus nextStatus() {
            return ENABLED;
        }
    };

    public abstract RestaurantStatus nextStatus();
}
