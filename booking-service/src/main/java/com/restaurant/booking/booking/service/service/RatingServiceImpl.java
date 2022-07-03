package com.restaurant.booking.booking.service.service;

import com.restaurant.booking.booking.model.*;
import com.restaurant.booking.booking.service.exception.RatingNotFoundException;
import com.restaurant.booking.booking.service.exception.ReservationAlreadyRatedException;
import com.restaurant.booking.booking.service.repository.RatingRepository;
import com.restaurant.booking.feign.client.RestaurantProxy;
import com.restaurant.booking.jwt.utils.JwtUtils;
import com.restaurant.booking.restaurant.model.AverageRatingUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final ReservationService reservationService;
    private final KafkaTemplate<String, AverageRatingUpdateRequest> kafkaTemplate;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository, ReservationService reservationService, KafkaTemplate kafkaTemplate) {
        this.ratingRepository = ratingRepository;
        this.reservationService = reservationService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public Rating createRating(RatingCreationRequest requestCreation) {

        log.info("Creating rating for reservation {}", requestCreation.getReservationId());

        Reservation reservation = reservationService.findReservation(requestCreation.getReservationId());

        if(Boolean.TRUE.equals(reservation.getRated())){
            throw new ReservationAlreadyRatedException(requestCreation.getReservationId());
        }
        reservationService.addRating(reservation);

        Rating rating = save(new Rating(requestCreation));

        updateRestaurantAverageRating(requestCreation.getRestaurantId());

        return rating;
    }

    @Override
    public List<Rating> getRestaurantRatings(String restaurantId) {

        log.info("Getting all restaurant's {} ratings", restaurantId);
        return ratingRepository.findAllByRestaurantId(restaurantId)
                .stream()
                .filter(rating -> rating.getRatingStatus() == RatingStatus.OK || rating.getRatingStatus() == RatingStatus.FLAGGED)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rating> getFlagedRatings() {

        log.info("Getting flaged ratings");
        return ratingRepository.findAllByRatingStatus(RatingStatus.FLAGGED);
    }

    @Override
    public Rating changeRatingStatus(RatingStatus ratingStatus, String ratingId) {

        log.info("Changing rating {} to {}", ratingId, ratingStatus);
        Rating rating = ratingRepository.findById(ratingId).orElseThrow(() -> new RatingNotFoundException(ratingId));
        rating.setRatingStatus(ratingStatus);
        rating = ratingRepository.save(rating);

        if(rating.getRatingStatus().equals(RatingStatus.CANCELLED))
            updateRestaurantAverageRating(rating.getRestaurantId());

        return rating;
    }

    private void updateRestaurantAverageRating(String restaurantId) {

        log.info("Updating the average rating of restaurant {}", restaurantId);

        List<Rating> restaurantRatings = ratingRepository.findAllByRestaurantId(restaurantId)
                .stream()
                .filter(rating -> rating.getRatingStatus() == RatingStatus.OK || rating.getRatingStatus() == RatingStatus.FLAGGED)
                .collect(Collectors.toList());

        Double averageRating = restaurantRatings
                .stream()
                .mapToDouble(Rating::getValue)
                .average()
                .orElse(0.0);

        Integer numRatings = restaurantRatings.size();

        kafkaTemplate.send("average-rating", new AverageRatingUpdateRequest(restaurantId, averageRating, numRatings));
    }
}
