package com.restaurant.booking.user.service.repository;

import com.restaurant.booking.user.model.Role;
import com.restaurant.booking.user.model.RoleName;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByName(RoleName name);
}
