package com.restaurant.booking.booking.service.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restaurant.booking.restaurant.model.AverageRatingUpdateRequest;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class AverageRatingSerializer implements Serializer<AverageRatingUpdateRequest> {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public byte[] serialize(String s, AverageRatingUpdateRequest ratingUpdateRequest) {

        if(ratingUpdateRequest == null)
            return null;
        try {
            return objectMapper.writeValueAsBytes(ratingUpdateRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
