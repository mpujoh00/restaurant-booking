package com.restaurant.booking.user.service.repository;

import com.restaurant.booking.user.model.Role;
import com.restaurant.booking.user.model.RoleName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
public class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;

    @Test
    void findByName(){
        roleRepository.save(new Role(RoleName.ROLE_ADMIN));
        Assertions.assertTrue(roleRepository.findByName(RoleName.ROLE_ADMIN).isPresent());
    }
}
