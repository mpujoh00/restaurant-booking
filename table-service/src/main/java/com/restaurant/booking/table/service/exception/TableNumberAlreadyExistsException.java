package com.restaurant.booking.table.service.exception;

public class TableNumberAlreadyExistsException extends RuntimeException {
    public TableNumberAlreadyExistsException(String restaurantId, Integer number) {
        super("Can't create table for restaurant with id " + restaurantId + ", it already has a table with number " + number);
    }
}
