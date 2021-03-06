package com.restaurant.booking.feign.client;

import com.restaurant.booking.table.model.Table;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "table-service")
public interface TableProxy {

    @GetMapping("/api/v1/tables/restaurant/{restaurantId}")
    List<Table> getRestaurantTables(@RequestHeader(value = "Authorization") String authorizationHeader, @PathVariable(value = "restaurantId") String restaurantId);
}
