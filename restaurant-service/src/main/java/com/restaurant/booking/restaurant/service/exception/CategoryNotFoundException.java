package com.restaurant.booking.restaurant.service.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String categoryName) {
        super("Category named " + categoryName + " not found");
    }
}
