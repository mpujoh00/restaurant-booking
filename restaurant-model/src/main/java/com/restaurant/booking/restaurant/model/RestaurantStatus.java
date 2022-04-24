package com.restaurant.booking.restaurant.model;

import java.util.Set;

public enum RestaurantStatus {
    PENDING{
        @Override
        public Set<RestaurantStatus> allowedStatus() {
            return Set.of(ENABLED);
        }
    },
    ENABLED {
        @Override
        public Set<RestaurantStatus> allowedStatus() {
            return Set.of(DISABLED);
        }
    },
    DISABLED {
        @Override
        public Set<RestaurantStatus> allowedStatus() {
            return Set.of(ENABLED);
        }
    };

    public abstract Set<RestaurantStatus> allowedStatus();
}
