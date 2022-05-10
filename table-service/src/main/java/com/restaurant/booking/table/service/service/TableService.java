package com.restaurant.booking.table.service.service;

import com.restaurant.booking.table.model.Table;
import com.restaurant.booking.table.model.TableCreationRequest;

import java.util.List;

public interface TableService {

    Table save(Table table);

    Table create(String restaurantId, TableCreationRequest tableCreationRequest);

    Table findTable(String tableId);

    List<Table> findAllRestaurantTables(String restaurantId);

    void deleteTable(String tableId);
}
