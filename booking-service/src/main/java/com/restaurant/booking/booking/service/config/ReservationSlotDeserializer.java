package com.restaurant.booking.booking.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class ReservationSlotDeserializer implements Deserializer<ReservSlotsCreationRequest> {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public ReservSlotsCreationRequest deserialize(String s, byte[] bytes) {

        if(bytes == null)
            return null;
        try {
            return objectMapper.readValue(bytes, ReservSlotsCreationRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
