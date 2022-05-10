package com.restaurant.booking.table.service.repository;

import com.restaurant.booking.table.model.Table;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TableRepository extends MongoRepository<Table, String> {

    List<Table> findAllByRestaurantId(String restaurantId);
    Optional<Table> findByRestaurantIdAndNumber(String restaurantId, Integer number);
}
