package com.restaurant.booking.bookingservice.service;

import com.restaurant.booking.booking.model.Reservation;
import com.restaurant.booking.booking.model.ReservationCreationRequest;
import com.restaurant.booking.booking.model.ReservationStatus;

import java.util.List;

public interface ReservationService {

    Reservation createReservation(ReservationCreationRequest creationRequest);

    Reservation save(Reservation reservation);

    Reservation findReservation(String reservationId);

    List<Reservation> findRestaurantReservations(String restaurantId);

    List<Reservation> findActiveUserReservations(String userId);

    List<Reservation> findInactiveUserReservations(String userId);

    Reservation changeReservationStatus(Reservation reservation, ReservationStatus status);
}
