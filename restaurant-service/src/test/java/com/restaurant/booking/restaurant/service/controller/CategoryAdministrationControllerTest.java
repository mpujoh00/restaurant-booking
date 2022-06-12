package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.Category;
import com.restaurant.booking.restaurant.service.service.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CategoryAdministrationControllerTest {

    @Mock
    private CategoryServiceImpl categoryService;

    @InjectMocks
    private CategoryAdministrationControllerImpl categoryController;

    @Test
    void createCategory(){
        Category category = new Category("category");

        Mockito.when(categoryService.createCategory("category")).thenReturn(category);

        ResponseEntity<Category> responseEntity = categoryController.createCategory("category");

        Mockito.verify(categoryService).createCategory("category");
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(category, responseEntity.getBody());
    }
}
