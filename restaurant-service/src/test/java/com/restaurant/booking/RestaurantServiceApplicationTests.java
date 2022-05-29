package com.restaurant.booking;

import com.restaurant.booking.restaurant.service.controller.RestaurantController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestaurantServiceApplicationTests {

	@Autowired
	private RestaurantController restaurantController;

	@Test
	void contextLoads() {
		RestaurantServiceApplication.main(new String[]{});
		Assertions.assertNotNull(restaurantController);
	}

}
