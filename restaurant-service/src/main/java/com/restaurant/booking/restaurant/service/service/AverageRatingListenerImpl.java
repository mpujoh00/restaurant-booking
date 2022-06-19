package com.restaurant.booking.restaurant.service.service;

import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import com.restaurant.booking.restaurant.model.AverageRatingUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AverageRatingListenerImpl implements AverageRatingListener {

    private final RestaurantService restaurantService;

    @Autowired
    public AverageRatingListenerImpl(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Override
    @KafkaListener(topics = "average-rating", groupId = "average-rating-service", containerFactory = "kafkaListenerFactory")
    public void updateAverageRating(AverageRatingUpdateRequest request) {

        log.info("Received average rating kafka event");
        restaurantService.updateRestaurantRating(request);
    }
}
