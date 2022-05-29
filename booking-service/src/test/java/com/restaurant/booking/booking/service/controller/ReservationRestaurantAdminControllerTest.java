package com.restaurant.booking.booking.service.controller;

import com.restaurant.booking.booking.model.Reservation;
import com.restaurant.booking.booking.model.ReservationStatus;
import com.restaurant.booking.booking.service.service.ReservationServiceImpl;
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
class ReservationRestaurantAdminControllerTest {

    @Mock
    private ReservationServiceImpl reservationService;

    @InjectMocks
    private ReservationRestaurantAdminControllerImpl reservationAdminController;

    @Test
    void getAllRestaurantReservations(){
        List<Reservation> reservations = List.of(new Reservation());

        Mockito.when(reservationService.findRestaurantReservations("id")).thenReturn(reservations);

        ResponseEntity<List<Reservation>> responseEntity = reservationAdminController.getAllRestaurantReservations("id");

        Mockito.verify(reservationService).findRestaurantReservations("id");
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(reservations, responseEntity.getBody());
    }

    @Test
    void changeReservationStatus(){
        Reservation reservation = new Reservation();

        Mockito.when(reservationService.findReservation("id")).thenReturn(reservation);
        Mockito.when(reservationService.changeReservationStatus(reservation, ReservationStatus.PENDING)).thenReturn(reservation);

        ResponseEntity<Reservation> responseEntity = reservationAdminController.changeReservationStatus("id", ReservationStatus.PENDING);

        Mockito.verify(reservationService).findReservation("id");
        Mockito.verify(reservationService).changeReservationStatus(reservation, ReservationStatus.PENDING);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(reservation, responseEntity.getBody());
    }
}
