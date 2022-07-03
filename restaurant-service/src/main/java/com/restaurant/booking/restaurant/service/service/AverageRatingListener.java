package com.restaurant.booking.restaurant.service.service;

import com.restaurant.booking.restaurant.model.AverageRatingUpdateRequest;

public interface AverageRatingListener {

    void updateAverageRating(AverageRatingUpdateRequest request);
}
