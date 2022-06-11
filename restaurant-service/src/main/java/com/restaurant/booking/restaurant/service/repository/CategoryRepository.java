package com.restaurant.booking.restaurant.service.repository;

import com.restaurant.booking.restaurant.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {

    Optional<Category> findByName(String name);
    boolean existsByName(String name);
}
