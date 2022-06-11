package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.Category;
import com.restaurant.booking.restaurant.service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryAdministrationControllerImpl implements CategoryAdministrationController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryAdministrationControllerImpl(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public ResponseEntity<Category> createCategory(String categoryName) {
        return ResponseEntity.ok(categoryService.createCategory(categoryName));
    }
}
