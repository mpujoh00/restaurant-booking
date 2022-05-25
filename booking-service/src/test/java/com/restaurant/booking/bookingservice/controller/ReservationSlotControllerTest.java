package com.restaurant.booking.bookingservice.controller;

import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import com.restaurant.booking.bookingservice.service.ReservationSlotServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ReservationSlotControllerTest {

    @Mock
    private ReservationSlotServiceImpl slotService;

    @InjectMocks
    private ReservationSlotControllerImpl reservationSlotController;

    @Test
    void generateRestaurantSlots(){
        ReservSlotsCreationRequest request = new ReservSlotsCreationRequest();

        ResponseEntity<Void> responseEntity = reservationSlotController.generateRestaurantSlots(request);

        Mockito.verify(slotService).createRestaurantSlots(request);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void generateRestaurantTableSlots(){
        ReservSlotsCreationRequest request = new ReservSlotsCreationRequest();

        ResponseEntity<Void> responseEntity = reservationSlotController.generateRestaurantSlots(request);

        Mockito.verify(slotService).createRestaurantSlots(request);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getRestaurantSlotsByPeople(){
        List<LocalTime> times = List.of(LocalTime.now());

        Mockito.when(slotService.findAllAvailableSlotsByPeople("id", 2, LocalDate.now())).thenReturn(times);

        ResponseEntity<List<LocalTime>> responseEntity = reservationSlotController.getRestaurantSlotsByPeople("id", 2, LocalDate.now());

        Mockito.verify(slotService).findAllAvailableSlotsByPeople("id", 2, LocalDate.now());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(times, responseEntity.getBody());
    }
}
