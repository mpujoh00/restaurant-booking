package com.restaurant.booking.user.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionAdvice {

    @ResponseBody
    @ExceptionHandler({UserNotFoundException.class, RoleNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundHandler(RuntimeException e){
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler({UserAlreadyExistsException.class, InvalidUserRegistration.class, IncorrectAdminDeletion.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badRequestHandler(RuntimeException e){
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler({UserLoginDisabledException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String forbiddenHandler(RuntimeException e){
        return e.getMessage();
    }
}
