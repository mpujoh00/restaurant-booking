package com.restaurant.booking.booking.service.service;

import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReservationSlotListenerImpl implements ReservationSlotListener {

    private final ReservationSlotService reservationSlotService;

    @Autowired
    public ReservationSlotListenerImpl(ReservationSlotService reservationSlotService) {
        this.reservationSlotService = reservationSlotService;
    }

    @Override
    @KafkaListener(topics = "reservation-slots", groupId = "reservation-slot-service", containerFactory = "kafkaListenerFactory")
    public void receiveRestaurantSlotEvent(ReservSlotsCreationRequest request) {
        log.info("Received reservation slot kafka event");
        reservationSlotService.createRestaurantSlots(request);
    }
}
