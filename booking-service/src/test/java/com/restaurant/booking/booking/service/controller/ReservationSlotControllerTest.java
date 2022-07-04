package com.restaurant.booking.booking.service.controller;

import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import com.restaurant.booking.booking.service.service.ReservationSlotServiceImpl;
import com.restaurant.booking.table.model.Table;
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
    void generateRestaurantTableSlots() {
        ReservSlotsCreationRequest slotsCreationRequest = new ReservSlotsCreationRequest();

        ResponseEntity<Void> responseEntity = reservationSlotController.generateRestaurantTableSlots(slotsCreationRequest);

        Mockito.verify(slotService).createRestaurantSlots(slotsCreationRequest);
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

    @Test
    void deleteRestaurantTableSlots() {
        Table table = Table.builder().id("1234").build();

        ResponseEntity<Void> responseEntity = reservationSlotController.deleteRestaurantTableSlots(table);

        Mockito.verify(slotService).deleteTableSlots(table);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
