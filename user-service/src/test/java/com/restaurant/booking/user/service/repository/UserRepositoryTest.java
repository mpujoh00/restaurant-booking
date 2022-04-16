package com.restaurant.booking.user.service.repository;

import com.restaurant.booking.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

@DataMongoTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void cleanUp(){
        userRepository.deleteAll();
    }

    @Test
    void findByEmail(){

        User user = User.builder().email("micaela@gmail.com").build();

        userRepository.save(user);

        Optional<User> obtainedUser = userRepository.findByEmail("micaela@gmail.com");

        Assertions.assertTrue(obtainedUser.isPresent());
        Assertions.assertEquals(user.getEmail(), obtainedUser.get().getEmail());
    }

    @Test
    void existsByEmail(){

        userRepository.save(User.builder().email("micaela@gmail.com").build());

        Assertions.assertTrue(userRepository.existsByEmail("micaela@gmail.com"));
        Assertions.assertFalse(userRepository.existsByEmail("false@gmail.com"));
    }

    @Test
    void deleteByEmail(){

        User savedUser = userRepository.save(User.builder().email("micaela@gmail.com").build());
        Assertions.assertTrue(userRepository.findById(savedUser.getId()).isPresent());

        userRepository.deleteByEmail("micaela@gmail.com");
        Assertions.assertFalse(userRepository.findById(savedUser.getId()).isPresent());
    }
}
