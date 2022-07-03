package com.restaurant.booking.table.service.controller;

import com.restaurant.booking.table.model.Table;
import com.restaurant.booking.table.model.TableCreationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Restaurant")
@RequestMapping("/api/v1/tables")
public interface TableController {

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Creates a table for a given restaurant", operationId = "createTable")
    @PostMapping("/{restaurantId}")
    ResponseEntity<Table> createTable(@PathVariable String restaurantId, @RequestBody @Valid TableCreationRequest tableCreationRequest);

    @Operation(description = "Gets a table", operationId = "getTable")
    @GetMapping("/{tableId}")
    ResponseEntity<Table> getTable(@PathVariable String tableId);

    @Operation(description = "Gets all tables of a restaurant", operationId = "getRestaurantTables")
    @GetMapping("/restaurant/{restaurantId}")
    ResponseEntity<List<Table>> getRestaurantTables(@PathVariable String restaurantId);

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Deletes a table", operationId = "deleteTable")
    @DeleteMapping("/delete/{tableId}")
    ResponseEntity<Void> deleteTable(@PathVariable String tableId);
}
