package com.restaurant.booking.feign.client;

import feign.Response;
import feign.codec.ErrorDecoder;

public class ResponseErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {

        return null;
    }
}
