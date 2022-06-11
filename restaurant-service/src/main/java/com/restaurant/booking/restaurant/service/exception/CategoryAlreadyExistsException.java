package com.restaurant.booking.restaurant.service.exception;

public class CategoryAlreadyExistsException extends RuntimeException {

    public CategoryAlreadyExistsException(String categoryName) {
        super("There already exists a category named " + categoryName);
    }
}
