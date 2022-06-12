package com.restaurant.booking.restaurant.service.service;

import com.restaurant.booking.restaurant.model.Category;
import com.restaurant.booking.restaurant.service.exception.CategoryAlreadyExistsException;
import com.restaurant.booking.restaurant.service.exception.CategoryNotFoundException;
import com.restaurant.booking.restaurant.service.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void getAllCategories(){
        List<Category> categories = List.of(new Category("category"));

        Mockito.when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> obtainedCategories = categoryService.getAllCategories();

        Mockito.verify(categoryRepository).findAll();
        Assertions.assertEquals(categories, obtainedCategories);
    }

    @Test
    void getCategory(){
        Category category = new Category("category");

        Mockito.when(categoryRepository.findByName("category")).thenReturn(Optional.of(category));

        Category obtainedCategory = categoryService.getCategory("category");

        Mockito.verify(categoryRepository).findByName("category");
        Assertions.assertEquals(category, obtainedCategory);
    }

    @Test
    void getCategory_notFound(){
        Mockito.when(categoryRepository.findByName("category")).thenReturn(Optional.empty());

        CategoryNotFoundException exception = Assertions.assertThrows(
                CategoryNotFoundException.class, () -> categoryService.getCategory("category"));
        Assertions.assertEquals("Category named category not found", exception.getMessage());

        Mockito.verify(categoryRepository).findByName("category");
    }

    @Test
    void createCategory(){
        Category category = new Category("category");

        Mockito.when(categoryRepository.existsByName("category")).thenReturn(false);
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);

        Category obtainedCategory = categoryService.createCategory("category");

        Mockito.verify(categoryRepository).existsByName("category");
        Mockito.verify(categoryRepository).save(Mockito.any());
        Assertions.assertEquals(category, obtainedCategory);
    }

    @Test
    void createCategory_alreadyExists(){
        Mockito.when(categoryRepository.existsByName("category")).thenReturn(true);

        CategoryAlreadyExistsException exception = Assertions.assertThrows(
                CategoryAlreadyExistsException.class, () -> categoryService.createCategory("category"));
        Assertions.assertEquals("There already exists a category named category", exception.getMessage());

        Mockito.verify(categoryRepository).existsByName("category");
    }
}
