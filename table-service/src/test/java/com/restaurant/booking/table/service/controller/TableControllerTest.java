package com.restaurant.booking.table.service.controller;

import com.restaurant.booking.table.model.Table;
import com.restaurant.booking.table.model.TableCreationRequest;
import com.restaurant.booking.table.service.service.TableServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class TableControllerTest {

    @Mock
    private TableServiceImpl tableService;

    @InjectMocks
    private TableControllerImpl tableController;

    @Test
    void createTable(){
        TableCreationRequest request = new TableCreationRequest(12, 2, 4);
        Table table = new Table(request, "id");

        Mockito.when(tableService.create("id", request)).thenReturn(table);

        Table obtainedTable = tableController.createTable("id", request).getBody();

        Mockito.verify(tableService).create("id", request);
        Assertions.assertEquals(table, obtainedTable);
    }

    @Test
    void getTable(){
        Table table = Table.builder().restaurantId("id").number(12).build();

        Mockito.when(tableService.findTable("id")).thenReturn(table);

        Table obtainedTable = tableController.getTable("id").getBody();

        Mockito.verify(tableService).findTable("id");
        Assertions.assertEquals(table, obtainedTable);
    }

    @Test
    void getRestaurantTables(){
        List<Table> tables = List.of(Table.builder().restaurantId("id").number(12).build(),
                Table.builder().restaurantId("id2").number(6).build());

        Mockito.when(tableService.findAllRestaurantTables("id")).thenReturn(tables);

        List<Table> obtainedTables = tableController.getRestaurantTables("id").getBody();

        Mockito.verify(tableService).findAllRestaurantTables("id");
        Assertions.assertEquals(tables, obtainedTables);
    }

    @Test
    void deleteTable(){
        Table table = new Table();

        Mockito.when(tableService.findTable("id")).thenReturn(table);

        HttpStatus obtainedStatus = tableController.deleteTable("id").getStatusCode();

        Mockito.verify(tableService).deleteTable(table);
        Assertions.assertEquals(HttpStatus.OK, obtainedStatus);
    }

}
