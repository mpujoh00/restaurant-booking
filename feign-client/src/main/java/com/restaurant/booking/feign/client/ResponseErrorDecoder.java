package com.restaurant.booking.feign.client;

import com.restaurant.booking.feign.client.exception.BadRequestException;
import com.restaurant.booking.feign.client.exception.NotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ResponseErrorDecoder implements ErrorDecoder {

    private ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {

        if(response.status() == HttpStatus.NOT_FOUND.value())
            return new NotFoundException();
        else if(response.status() == HttpStatus.BAD_REQUEST.value())
            return new BadRequestException();

        return errorDecoder.decode(s, response);
    }
}
