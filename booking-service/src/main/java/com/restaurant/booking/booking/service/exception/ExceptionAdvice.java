package com.restaurant.booking.booking.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionAdvice {

    @ResponseBody
    @ExceptionHandler({ReservationSlotNotFoundException.class, ReservationNotFoundException.class, RatingNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundHandler(RuntimeException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler({InvalidReservationStatusException.class, NoSlotAvailableException.class, ReservationAlreadyRatedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badRequestHandler(RuntimeException e) {
        return e.getMessage();
    }
}