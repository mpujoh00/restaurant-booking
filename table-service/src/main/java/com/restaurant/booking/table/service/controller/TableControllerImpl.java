package com.restaurant.booking.table.service.controller;

import com.restaurant.booking.table.model.Table;
import com.restaurant.booking.table.model.TableCreationRequest;
import com.restaurant.booking.table.service.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TableControllerImpl implements TableController {

    private final TableService tableService;

    @Autowired
    public TableControllerImpl(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public ResponseEntity<Table> createTable(String restaurantId, TableCreationRequest tableCreationRequest) {
        return ResponseEntity.ok(tableService.create(restaurantId, tableCreationRequest));
    }

    @Override
    public ResponseEntity<Table> getTable(String tableId) {
        return ResponseEntity.ok(tableService.findTable(tableId));
    }

    @Override
    public ResponseEntity<List<Table>> getRestaurantTables(String restaurantId) {
        return ResponseEntity.ok(tableService.findAllRestaurantTables(restaurantId));
    }

    @Override
    public ResponseEntity<Void> deleteTable(String tableId) {
        tableService.deleteTable(tableService.findTable(tableId));
        return ResponseEntity.ok().build();
    }
}
