package com.restaurant.booking.booking.service.service;

import com.restaurant.booking.booking.model.Rating;
import com.restaurant.booking.booking.model.RatingRequestCreation;
import com.restaurant.booking.booking.model.Reservation;
import com.restaurant.booking.booking.model.RestaurantRating;
import com.restaurant.booking.booking.service.exception.ReservationAlreadyRatedException;
import com.restaurant.booking.booking.service.repository.RatingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    private final ReservationService reservationService;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository, ReservationService reservationService) {
        this.ratingRepository = ratingRepository;
        this.reservationService = reservationService;
    }

    @Override
    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public Rating createRating(RatingRequestCreation requestCreation) {

        log.info("Creating rating for reservation {}", requestCreation.getReservationId());

        Reservation reservation = reservationService.findReservation(requestCreation.getReservationId());

        if(Boolean.TRUE.equals(reservation.getRated())){
            throw new ReservationAlreadyRatedException(requestCreation.getReservationId());
        }
        reservationService.addRating(reservation);
        return save(new Rating(requestCreation));
    }

    @Override
    public List<Rating> getRestaurantRatings(String restaurantId) {

        log.info("Getting all restaurant's {} ratings", restaurantId);
        return ratingRepository.findAllByRestaurantId(restaurantId);
    }

    @Override
    public RestaurantRating getRestaurantAverageRating(String restaurantId) {

        log.info("Getting the average rating of restaurant {}", restaurantId);

        List<Rating> restaurantRatings = ratingRepository.findAllByRestaurantId(restaurantId);

        Double averageRating = restaurantRatings
                .stream()
                .mapToInt(Rating::getValue)
                .average()
                .orElse(0.0);

        Integer numRatings = restaurantRatings.size();

        return new RestaurantRating(averageRating, numRatings);
    }
}
