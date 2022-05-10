package com.restaurant.booking.feign.client;

import com.restaurant.booking.user.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "user-service")
public interface UserProxy {

    @GetMapping("api/v1/users/{email}")
    User getUserByEmail(@PathVariable String email);

    @PutMapping("api/v1/users/add-restaurant/{restaurantId}/user/{userEmail}")
    void addRestaurant(@PathVariable String userEmail, @PathVariable String restaurantId);
}
