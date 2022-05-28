package com.restaurant.booking;

import com.restaurant.booking.api.gateway.config.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiGatewayApplicationTests {

	@Autowired
	private JwtUtils jwtUtils;

	@Test
	void contextLoads() {
		ApiGatewayApplication.main(new String[]{});
		Assertions.assertNotNull(jwtUtils);
	}

}
