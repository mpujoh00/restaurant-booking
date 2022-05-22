package com.restaurant.booking.bookingservice.controller;

import com.restaurant.booking.booking.model.Reservation;
import com.restaurant.booking.booking.model.ReservationCreationRequest;
import com.restaurant.booking.bookingservice.service.ReservationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @Mock
    private ReservationServiceImpl reservationService;

    @InjectMocks
    private ReservationControllerImpl reservationController;

    @Test
    void createReservation(){

        ReservationCreationRequest creationRequest = new ReservationCreationRequest();
        Reservation reservation = new Reservation();

        Mockito.when(reservationService.createReservation(creationRequest)).thenReturn(reservation);

        ResponseEntity<Reservation> responseEntity = reservationController.createReservation(creationRequest);

        Mockito.verify(reservationService).createReservation(creationRequest);
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertEquals(reservation, responseEntity.getBody());
    }
}
