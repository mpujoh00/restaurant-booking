package com.restaurant.booking.table.service.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class ReservationSlotSerializer implements Serializer<ReservSlotsCreationRequest> {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public byte[] serialize(String s, ReservSlotsCreationRequest slotsCreationRequest) {

        if(slotsCreationRequest == null)
            return null;
        try {
            return objectMapper.writeValueAsBytes(slotsCreationRequest);
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
