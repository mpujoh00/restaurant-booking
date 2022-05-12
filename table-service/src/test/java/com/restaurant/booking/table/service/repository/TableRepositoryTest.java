package com.restaurant.booking.table.service.repository;

import com.restaurant.booking.table.model.Table;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class TableRepositoryTest {

    @Autowired
    private TableRepository tableRepository;

    @AfterEach
    void cleanUp(){
        tableRepository.deleteAll();
    }

    @Test
    void findAllRestaurantTables(){

        List<Table> tables = List.of(Table.builder().restaurantId("id").number(12).build(),
                Table.builder().restaurantId("id2").number(6).build());

        tableRepository.saveAll(tables);

        List<Table> obtainedTables = tableRepository.findAllByRestaurantId("id");

        Assertions.assertEquals(List.of(tables.get(0)), obtainedTables);
    }

    @Test
    void findRestaurantTable(){

        Table table = Table.builder().restaurantId("id").number(12).build();

        tableRepository.save(table);

        Optional<Table> obtainedTable = tableRepository.findByRestaurantIdAndNumber("id", 12);

        Assertions.assertTrue(obtainedTable.isPresent());
        Assertions.assertEquals(table, obtainedTable.get());
    }
}
