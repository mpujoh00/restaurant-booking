package com.restaurant.booking.booking.service.controller;

import com.restaurant.booking.booking.model.RatingCreationRequest;
import com.restaurant.booking.booking.model.RatingStatus;
import com.restaurant.booking.booking.service.service.RatingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RatingControllerTest {

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingControllerImpl ratingController;

    @Test
    void createRating() {
        RatingCreationRequest ratingCreationRequest = RatingCreationRequest.builder().reservationId("123").value(5.0).build();
        ratingController.createRating(ratingCreationRequest);
        Mockito.verify(ratingService).createRating(ratingCreationRequest);
    }

    @Test
    void getRestaurantRatings() {
        ratingController.getRestaurantRatings("1234");
        Mockito.verify(ratingService).getRestaurantRatings("1234");
    }

    @Test
    void getFlaggedRatings() {
        ratingController.getFlaggedRatings();
        Mockito.verify(ratingService).getFlagedRatings();
    }

    @Test
    void flagRating() {
        ratingController.flagRating("1234");
        Mockito.verify(ratingService).changeRatingStatus(RatingStatus.FLAGGED, "1234");
    }

    @Test
    void changeRatingStatus() {
        ratingController.changeRatingStatus("1234", RatingStatus.CANCELLED);
        Mockito.verify(ratingService).changeRatingStatus(RatingStatus.CANCELLED, "1234");
    }
}