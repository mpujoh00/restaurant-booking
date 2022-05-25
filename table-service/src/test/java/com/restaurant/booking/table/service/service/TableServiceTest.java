package com.restaurant.booking.table.service.service;

import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import com.restaurant.booking.feign.client.BookingProxy;
import com.restaurant.booking.feign.client.RestaurantProxy;
import com.restaurant.booking.table.model.Table;
import com.restaurant.booking.table.model.TableCreationRequest;
import com.restaurant.booking.table.service.exception.TableNotFoundException;
import com.restaurant.booking.table.service.exception.TableNumberAlreadyExistsException;
import com.restaurant.booking.table.service.repository.TableRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private TableRepository tableRepository;

    @Mock
    private BookingProxy bookingProxy;

    @Mock
    private RestaurantProxy restaurantProxy;

    @InjectMocks
    private TableServiceImpl tableService;

    @Test
    void create(){
        TableCreationRequest request = new TableCreationRequest(12, 2, 4);
        Table table = new Table(request, "id");
        List<LocalTime> reservationHours = List.of(LocalTime.now());
        ReservSlotsCreationRequest slotsCreationRequest = new ReservSlotsCreationRequest("id", reservationHours, table);

        Mockito.when(tableRepository.findByRestaurantIdAndNumber("id", request.getNumber())).thenReturn(Optional.empty());
        Mockito.when(tableRepository.save(table)).thenReturn(table);
        Mockito.when(restaurantProxy.getRestaurantsReservationHours("id")).thenReturn(reservationHours);

        Table obtainedTable = tableService.create("id", request);

        Mockito.verify(tableRepository).findByRestaurantIdAndNumber("id", request.getNumber());
        Mockito.verify(tableRepository).save(table);
        Mockito.verify(restaurantProxy).getRestaurantsReservationHours("id");
        Mockito.verify(bookingProxy).generateRestaurantTableSlots(slotsCreationRequest);
        Assertions.assertEquals(table, obtainedTable);
    }

    @Test
    void create_alreadyExists(){
        TableCreationRequest request = new TableCreationRequest(12, 2, 4);
        Table table = new Table(request, "id");

        Mockito.when(tableRepository.findByRestaurantIdAndNumber("id", request.getNumber())).thenReturn(Optional.of(table));

        TableNumberAlreadyExistsException exception = Assertions.assertThrows(TableNumberAlreadyExistsException.class,
                () -> tableService.create("id", request));
        Assertions.assertEquals("Can't create table for restaurant with id id, it already has a table with number 12",
                exception.getMessage());

        Mockito.verify(tableRepository).findByRestaurantIdAndNumber("id", request.getNumber());
    }

    @Test
    void findTable(){
        Table table = Table.builder().id("id").build();

        Mockito.when(tableRepository.findById("id")).thenReturn(Optional.of(table));

        Table obtainedTable = tableService.findTable("id");

        Mockito.verify(tableRepository).findById("id");
        Assertions.assertEquals(table, obtainedTable);
    }

    @Test
    void findTable_notFound(){
        Table table = Table.builder().id("id").build();

        Mockito.when(tableRepository.findById("id")).thenReturn(Optional.empty());

        TableNotFoundException exception = Assertions.assertThrows(TableNotFoundException.class,
                () -> tableService.findTable("id"));
        Assertions.assertEquals("Table with id id not found",
                exception.getMessage());

        Mockito.verify(tableRepository).findById("id");
    }

    @Test
    void findAllRestaurantTables(){

        List<Table> tables = List.of(Table.builder().restaurantId("id").number(12).build(),
                Table.builder().restaurantId("id2").number(6).build());

        Mockito.when(tableRepository.findAllByRestaurantId("id")).thenReturn(tables);

        List<Table> obtainedTables = tableService.findAllRestaurantTables("id");

        Mockito.verify(tableRepository).findAllByRestaurantId("id");
        Assertions.assertEquals(tables, obtainedTables);
    }

    @Test
    void delete(){
        Table table = Table.builder().id("id").build();

        tableService.deleteTable(table);

        Mockito.verify(bookingProxy).deleteRestaurantTableSlots(table);
        Mockito.verify(tableRepository).deleteById("id");
    }
}
