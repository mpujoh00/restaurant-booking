package com.restaurant.booking.bookingservice.repository;

import com.restaurant.booking.booking.model.Reservation;
import com.restaurant.booking.booking.model.ReservationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReservationRepository extends MongoRepository<Reservation, String> {

    List<Reservation> findAllByRestaurantId(String restaurantId);
    List<Reservation> findAllByUserIdAndStatusIn(String userId, List<ReservationStatus> statuses);
    List<Reservation> findAllByUserId(String userId);
}
