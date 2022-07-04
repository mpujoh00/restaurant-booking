package com.restaurant.booking.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.booking.user.model.RegistrationRequest;
import com.restaurant.booking.user.model.Role;
import com.restaurant.booking.user.model.RoleName;
import com.restaurant.booking.user.model.User;
import com.restaurant.booking.user.service.repository.RoleRepository;
import com.restaurant.booking.user.service.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@AutoConfigureDataMongo
@SpringBootTest
class UserServiceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        roleRepository.save(new Role(RoleName.ROLE_CLIENT));
    }

    @Test
    void registerUser() throws Exception {

        RegistrationRequest wrongRegistrationRequest = new RegistrationRequest("test@test.com", "12", "Test User", RoleName.ROLE_CLIENT);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(wrongRegistrationRequest)))
                .andExpect(status().isBadRequest());

        RegistrationRequest registrationRequest = new RegistrationRequest("test@test.com", "12345678", "Test User", RoleName.ROLE_CLIENT);

        Optional<User> userOpt = userRepository.findByEmail("test@test.com");
        Assertions.assertTrue(userOpt.isEmpty());

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated());

        userOpt = userRepository.findByEmail("test@test.com");
        Assertions.assertTrue(userOpt.isPresent());
        User user = userOpt.get();
        Assertions.assertEquals(registrationRequest.getEmail(), user.getEmail());
        Assertions.assertEquals(registrationRequest.getFullname(), user.getFullname());
        Assertions.assertNotEquals(registrationRequest.getPassword(), user.getPassword());
        Assertions.assertTrue(user.getRoles().stream().map(Role::getName).anyMatch(role -> role == registrationRequest.getRole()));
    }

}
