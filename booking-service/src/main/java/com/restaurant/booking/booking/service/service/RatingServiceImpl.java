package com.restaurant.booking.booking.service.service;

import com.restaurant.booking.booking.model.Rating;
import com.restaurant.booking.booking.model.RatingRequestCreation;
import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import com.restaurant.booking.booking.model.Reservation;
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

@Slf4j
@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final ReservationService reservationService;
    private final RestaurantProxy restaurantProxy;
    private final JwtUtils jwtUtils;
    private final KafkaTemplate<String, AverageRatingUpdateRequest> kafkaTemplate;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository, ReservationService reservationService, RestaurantProxy restaurantProxy, JwtUtils jwtUtils, KafkaTemplate kafkaTemplate) {
        this.ratingRepository = ratingRepository;
        this.reservationService = reservationService;
        this.restaurantProxy = restaurantProxy;
        this.jwtUtils = jwtUtils;
        this.kafkaTemplate = kafkaTemplate;
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

        Rating rating = save(new Rating(requestCreation));

        updateRestaurantAverageRating(requestCreation.getRestaurantId());

        return rating;
    }

    @Override
    public List<Rating> getRestaurantRatings(String restaurantId) {

        log.info("Getting all restaurant's {} ratings", restaurantId);
        return ratingRepository.findAllByRestaurantId(restaurantId);
    }

    private void updateRestaurantAverageRating(String restaurantId) {

        log.info("Updating the average rating of restaurant {}", restaurantId);

        List<Rating> restaurantRatings = ratingRepository.findAllByRestaurantId(restaurantId);

        Double averageRating = restaurantRatings
                .stream()
                .mapToInt(Rating::getValue)
                .average()
                .orElse(0.0);

        Integer numRatings = restaurantRatings.size();

        kafkaTemplate.send("average-rating", new AverageRatingUpdateRequest(restaurantId, averageRating, numRatings));
        //restaurantProxy.updateRestaurantAverageRating(jwtUtils.getAuthorizationHeader(), restaurantId, new AverageRatingUpdateRequest(averageRating, numRatings));
    }
}
