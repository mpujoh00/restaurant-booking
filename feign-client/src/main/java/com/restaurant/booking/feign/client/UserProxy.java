package com.restaurant.booking.feign.client;

import com.restaurant.booking.user.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserProxy {

    @GetMapping("api/v1/users/{email}")
    User getUserByEmail(@PathVariable String email);
}
