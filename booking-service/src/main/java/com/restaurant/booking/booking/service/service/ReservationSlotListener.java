package com.restaurant.booking.booking.service.service;

import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;

public interface ReservationSlotListener {

    void receiveRestaurantSlotEvent(ReservSlotsCreationRequest request);
}
