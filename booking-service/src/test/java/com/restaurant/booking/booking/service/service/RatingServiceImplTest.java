package com.restaurant.booking.booking.service.service;

import com.restaurant.booking.booking.model.Rating;
import com.restaurant.booking.booking.model.RatingCreationRequest;
import com.restaurant.booking.booking.model.RatingStatus;
import com.restaurant.booking.booking.model.Reservation;
import com.restaurant.booking.booking.service.exception.ReservationAlreadyRatedException;
import com.restaurant.booking.booking.service.repository.RatingRepository;
import com.restaurant.booking.restaurant.model.AverageRatingUpdateRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private ReservationService reservationService;

    @Mock
    private KafkaTemplate<String, AverageRatingUpdateRequest> kafkaTemplate;

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Test
    void save() {
        Rating rating = Rating.builder().comment("Comment").value(5.0).build();

        Mockito.when(ratingRepository.save(Mockito.eq(rating))).then(returnsFirstArg());

        Rating persistedRating = ratingService.save(rating);

        Mockito.verify(ratingRepository).save(Mockito.eq(rating));
        Assertions.assertEquals(rating, persistedRating);
    }

    @Test
    void createRating() {
        RatingCreationRequest ratingCreationRequest = RatingCreationRequest.builder()
                .reservationId("R123")
                .restaurantId("Restaurant1")
                .value(5.0)
                .build();
        AverageRatingUpdateRequest averageRatingUpdateRequest
                = new AverageRatingUpdateRequest("Restaurant1", 3.0, 4);

        Mockito.when(reservationService.findReservation("R123")).thenReturn(
                Reservation.builder().rated(false).build()
        );
        Mockito.when(ratingRepository.findAllByRestaurantId("Restaurant1")).thenReturn(List.of(
                Rating.builder().value(5.0).ratingStatus(RatingStatus.OK).build(),
                Rating.builder().value(3.0).ratingStatus(RatingStatus.OK).build(),
                Rating.builder().value(2.0).ratingStatus(RatingStatus.OK).build(),
                Rating.builder().value(2.0).ratingStatus(RatingStatus.FLAGGED).build(),
                Rating.builder().value(1.0).ratingStatus(RatingStatus.CANCELLED).build()
        ));

        ratingService.createRating(ratingCreationRequest);

        Mockito.verify(reservationService).findReservation("R123");
        Mockito.verify(reservationService).addRating(Reservation.builder().rated(false).build());
        Mockito.verify(ratingRepository).findAllByRestaurantId("Restaurant1");
        Mockito.verify(kafkaTemplate).send(Mockito.eq("average-rating"), Mockito.eq(averageRatingUpdateRequest));
    }


    @Test
    void createRating_alreadyRated() {
        RatingCreationRequest ratingCreationRequest = RatingCreationRequest.builder()
                .reservationId("R123")
                .restaurantId("Restaurant1")
                .value(5.0)
                .build();

        Mockito.when(reservationService.findReservation("R123")).thenReturn(
                Reservation.builder().rated(true).build()
        );

        Assertions.assertThrows(ReservationAlreadyRatedException.class,
                () -> ratingService.createRating(ratingCreationRequest));

        Mockito.verify(reservationService).findReservation("R123");
        Mockito.verify(reservationService, Mockito.never()).addRating(Reservation.builder().rated(false).build());
        Mockito.verify(ratingRepository, Mockito.never()).findAllByRestaurantId("Restaurant1");
        Mockito.verify(kafkaTemplate, Mockito.never()).send(Mockito.eq("average-rating"), Mockito.any());
    }

    @Test
    void getRestaurantRatings() {
        Mockito.when(ratingRepository.findAllByRestaurantId("R123")).thenReturn(List.of(
                Rating.builder().value(5.0).ratingStatus(RatingStatus.OK).build(),
                Rating.builder().value(2.0).ratingStatus(RatingStatus.FLAGGED).build(),
                Rating.builder().value(1.0).ratingStatus(RatingStatus.CANCELLED).build()
        ));

        List<Rating> ratings = ratingService.getRestaurantRatings("R123");

        Mockito.verify(ratingRepository).findAllByRestaurantId("R123");
        Assertions.assertIterableEquals(List.of(
                Rating.builder().value(5.0).ratingStatus(RatingStatus.OK).build(),
                Rating.builder().value(2.0).ratingStatus(RatingStatus.FLAGGED).build()
        ), ratings);
    }

    @Test
    void getFlagedRatings() {
        ratingService.getFlagedRatings();
        Mockito.verify(ratingRepository).findAllByRatingStatus(RatingStatus.FLAGGED);
    }

    @Test
    void changeRatingStatus() {
        Rating rating = Rating.builder().ratingStatus(RatingStatus.OK).build();
        Rating updatedRating = Rating.builder().ratingStatus(RatingStatus.FLAGGED).build();

        Mockito.when(ratingRepository.findById("123")).thenReturn(Optional.of(rating));
        Mockito.when(ratingRepository.save(Mockito.eq(updatedRating))).thenReturn(updatedRating);

        ratingService.changeRatingStatus(RatingStatus.FLAGGED, "123");

        Mockito.verify(ratingRepository).findById("123");
        Mockito.verify(ratingRepository).save(Mockito.eq(updatedRating));
        Mockito.verify(ratingRepository, Mockito.never()).findAllByRestaurantId("Restaurant1");
        Mockito.verify(kafkaTemplate, Mockito.never()).send(Mockito.eq("average-rating"), Mockito.any());
    }

    @Test
    void changeRatingStatus_toCancelled() {
        Rating rating = Rating.builder().restaurantId("Restaurant1").ratingStatus(RatingStatus.FLAGGED).build();
        Rating updatedRating = Rating.builder().restaurantId("Restaurant1").ratingStatus(RatingStatus.CANCELLED).build();
        AverageRatingUpdateRequest averageRatingUpdateRequest
                = new AverageRatingUpdateRequest("Restaurant1", 3.0, 4);


        Mockito.when(ratingRepository.findById("123")).thenReturn(Optional.of(rating));
        Mockito.when(ratingRepository.save(Mockito.eq(updatedRating))).thenReturn(updatedRating);
        Mockito.when(ratingRepository.findAllByRestaurantId("Restaurant1")).thenReturn(List.of(
                Rating.builder().value(5.0).ratingStatus(RatingStatus.OK).build(),
                Rating.builder().value(3.0).ratingStatus(RatingStatus.OK).build(),
                Rating.builder().value(2.0).ratingStatus(RatingStatus.OK).build(),
                Rating.builder().value(2.0).ratingStatus(RatingStatus.FLAGGED).build(),
                Rating.builder().value(1.0).ratingStatus(RatingStatus.CANCELLED).build()
        ));

        ratingService.changeRatingStatus(RatingStatus.CANCELLED, "123");

        Mockito.verify(ratingRepository).findById("123");
        Mockito.verify(ratingRepository).save(Mockito.eq(updatedRating));
        Mockito.verify(ratingRepository).findAllByRestaurantId("Restaurant1");
        Mockito.verify(kafkaTemplate).send(Mockito.eq("average-rating"), Mockito.eq(averageRatingUpdateRequest));
    }
}