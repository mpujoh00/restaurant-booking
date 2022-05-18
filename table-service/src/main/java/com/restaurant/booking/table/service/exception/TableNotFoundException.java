package com.restaurant.booking.table.service.exception;

public class TableNotFoundException extends RuntimeException {

    public TableNotFoundException(String tableId) {
        super("Table with id " + tableId + " not found");
    }
}
