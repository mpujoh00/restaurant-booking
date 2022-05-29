package com.restaurant.booking.booking.service;

import com.restaurant.booking.BookingServiceApplication;
import com.restaurant.booking.booking.service.controller.ReservationController;
import com.restaurant.booking.booking.service.service.ReservationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookingServiceApplicationTests {

	@Autowired
	ReservationController reservationController;

	@Autowired
	ReservationService reservationService;

	@Test
	void contextLoads() {
		BookingServiceApplication.main(new String[]{});
		Assertions.assertNotNull(reservationController);
		Assertions.assertNotNull(reservationService);
	}

}
