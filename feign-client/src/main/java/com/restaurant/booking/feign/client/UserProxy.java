package com.restaurant.booking.feign.client;

import com.restaurant.booking.user.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserProxy {

    @GetMapping("api/v1/users/{email}")
    User getUserByEmail(@PathVariable(value = "email") String email);

    @PutMapping("api/v1/users/add-restaurant/{restaurantId}/user/{userEmail}")
    void addRestaurant(@RequestHeader(value = "Authorization") String authorizationHeader, @PathVariable(value = "userEmail") String userEmail, @PathVariable(value = "restaurantId")  String restaurantId);
}
