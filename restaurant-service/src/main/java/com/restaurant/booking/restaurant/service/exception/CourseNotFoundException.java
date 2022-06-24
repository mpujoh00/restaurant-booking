package com.restaurant.booking.restaurant.service.exception;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(String id){

        super("Course with id " + id + " not found");
    }
}
