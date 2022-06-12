package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Tag(name = "Category")
@RequestMapping("/api/v1/admin/categories")
public interface CategoryAdministrationController {

    @Operation(description = "Creates a category", operationId = "createCategory")
    @PostMapping
    ResponseEntity<Category> createCategory(@RequestBody String categoryName);
}
