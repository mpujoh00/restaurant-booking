package com.restaurant.booking.bookingservice.controller;

import com.restaurant.booking.booking.model.Reservation;
import com.restaurant.booking.booking.model.ReservationCreationRequest;
import com.restaurant.booking.booking.model.ReservationStatus;
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

import java.util.List;

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

    @Test
    void getReservation(){
        Reservation reservation = new Reservation();

        Mockito.when(reservationService.findReservation("id")).thenReturn(reservation);

        ResponseEntity<Reservation> responseEntity = reservationController.getReservation("id");

        Mockito.verify(reservationService).findReservation("id");
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(reservation, responseEntity.getBody());
    }

    @Test
    void getActiveUserReservations(){
        List<Reservation> reservations = List.of(new Reservation());

        Mockito.when(reservationService.findActiveUserReservations("id")).thenReturn(reservations);

        ResponseEntity<List<Reservation>> responseEntity = reservationController.getActiveUserReservations("id");

        Mockito.verify(reservationService).findActiveUserReservations("id");
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(reservations, responseEntity.getBody());
    }

    @Test
    void getInactiveUserReservations(){
        List<Reservation> reservations = List.of(new Reservation());

        Mockito.when(reservationService.findInactiveUserReservations("id")).thenReturn(reservations);

        ResponseEntity<List<Reservation>> responseEntity = reservationController.getInactiveUserReservations("id");

        Mockito.verify(reservationService).findInactiveUserReservations("id");
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(reservations, responseEntity.getBody());
    }

    @Test
    void cancelReservation(){
        Reservation reservation = new Reservation();

        Mockito.when(reservationService.findReservation("id")).thenReturn(reservation);
        Mockito.when(reservationService.changeReservationStatus(reservation, ReservationStatus.CANCELED)).thenReturn(reservation);

        ResponseEntity<Reservation> responseEntity = reservationController.cancelReservation("id");

        Mockito.verify(reservationService).findReservation("id");
        Mockito.verify(reservationService).changeReservationStatus(reservation, ReservationStatus.CANCELED);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(reservation, responseEntity.getBody());
    }
}
