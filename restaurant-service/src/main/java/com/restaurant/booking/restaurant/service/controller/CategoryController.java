package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Category")
@RequestMapping("/api/v1/categories")
public interface CategoryController {

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(description = "Creates a category", operationId = "createCategory")
    @PostMapping
    ResponseEntity<Category> createCategory(@RequestBody String categoryName);

    @Operation(description = "Gets all categories", operationId = "getAllCategories")
    @GetMapping
    ResponseEntity<List<Category>> getAllCategories();
}
