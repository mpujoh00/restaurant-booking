package com.restaurant.booking.user.service.repository;

import com.restaurant.booking.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    void deleteByEmail(String email);
}
