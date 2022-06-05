package com.restaurant.booking.feign.client;

import com.restaurant.booking.user.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserProxy {

    @PutMapping("api/v1/users/add-restaurant/{restaurantId}/user/{userEmail}")
    void addRestaurant(@RequestHeader(value = "Authorization") String authorizationHeader, @PathVariable(value = "userEmail") String userEmail, @PathVariable(value = "restaurantId")  String restaurantId);

    @GetMapping("api/v1/users/username/{id}")
    String getUsername(@RequestHeader(value = "Authorization") String authorizationHeader, @PathVariable(value = "id") String id);

    @PutMapping("api/v1/admin/users/change-status/{email}")
    User updateUserStatus(@RequestHeader(value = "Authorization") String authorizationHeader, @PathVariable(value = "email") String email);
}
