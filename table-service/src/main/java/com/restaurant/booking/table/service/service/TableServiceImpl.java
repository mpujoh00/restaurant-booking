package com.restaurant.booking.table.service.service;

import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import com.restaurant.booking.feign.client.BookingProxy;
import com.restaurant.booking.feign.client.RestaurantProxy;
import com.restaurant.booking.jwt.utils.JwtUtils;
import com.restaurant.booking.table.model.Table;
import com.restaurant.booking.table.model.TableCreationRequest;
import com.restaurant.booking.table.service.exception.TableNotFoundException;
import com.restaurant.booking.table.service.exception.TableNumberAlreadyExistsException;
import com.restaurant.booking.table.service.repository.TableRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TableServiceImpl implements TableService {

    private final TableRepository tableRepository;
    private final BookingProxy bookingProxy;
    private final RestaurantProxy restaurantProxy;
    private final JwtUtils jwtUtils;
    private final KafkaTemplate<String, ReservSlotsCreationRequest> kafkaTemplate;

    @Autowired
    public TableServiceImpl(TableRepository tableRepository, BookingProxy bookingProxy, RestaurantProxy restaurantProxy,
                            JwtUtils jwtUtils, KafkaTemplate<String, ReservSlotsCreationRequest> kafkaTemplate) {
        this.tableRepository = tableRepository;
        this.bookingProxy = bookingProxy;
        this.restaurantProxy = restaurantProxy;
        this.jwtUtils = jwtUtils;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Table save(Table table) {
        log.info("Saving table for restaurant with id {}", table.getRestaurantId());
        return tableRepository.save(table);
    }

    @Override
    public Table create(String restaurantId, TableCreationRequest tableCreationRequest) {

        log.info("Creating new table for restaurant with id {}", restaurantId);

        // checks if restaurant already has a table with the given number
        if(tableRepository.findByRestaurantIdAndNumber(restaurantId, tableCreationRequest.getNumber()).isPresent())
                throw new TableNumberAlreadyExistsException(restaurantId, tableCreationRequest.getNumber());

        Table table = new Table(tableCreationRequest, restaurantId);
        table = save(table);

        // generates its reservation slots
        kafkaTemplate.send("reservation-slots", new ReservSlotsCreationRequest(restaurantId,
                restaurantProxy.getRestaurantsReservationHours(jwtUtils.getAuthorizationHeader(), restaurantId), table));

        return table;
    }

    @Override
    public Table findTable(String tableId) {

        log.info("Getting table with id {}", tableId);
        return tableRepository.findById(tableId).orElseThrow(() -> new TableNotFoundException(tableId));
    }

    @Override
    public List<Table> findAllRestaurantTables(String restaurantId) {

        log.info("Getting all tables from restaurant with id {}", restaurantId);
        return tableRepository.findAllByRestaurantId(restaurantId);
    }

    @Override
    public void deleteTable(Table table) {

        log.info("Deleting table with id {}", table.getId());
        // deletes its future reservation slots
        bookingProxy.deleteRestaurantTableSlots(jwtUtils.getAuthorizationHeader(), table);
        tableRepository.deleteById(table.getId());
    }
}
