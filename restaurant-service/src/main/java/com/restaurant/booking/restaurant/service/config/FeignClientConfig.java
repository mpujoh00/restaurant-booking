package com.restaurant.booking.restaurant.service.config;

import com.restaurant.booking.feign.client.ResponseErrorDecoder;
import com.restaurant.booking.feign.client.exception.FeignExceptionAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public ResponseErrorDecoder responseErrorDecoder(){
        return new ResponseErrorDecoder();
    }

    @Bean
    public FeignExceptionAdvice feignExceptionAdvice(){
        return new FeignExceptionAdvice();
    }
}
