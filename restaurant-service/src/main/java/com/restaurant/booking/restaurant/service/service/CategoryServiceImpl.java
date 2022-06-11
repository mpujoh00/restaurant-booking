package com.restaurant.booking.restaurant.service.service;

import com.restaurant.booking.restaurant.model.Category;
import com.restaurant.booking.restaurant.service.exception.CategoryAlreadyExistsException;
import com.restaurant.booking.restaurant.service.exception.CategoryNotFoundException;
import com.restaurant.booking.restaurant.service.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        log.info("Getting all categories");
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategory(String name) {
        log.info("Getting category: {}", name);
        return categoryRepository.findByName(name).orElseThrow(() -> new CategoryNotFoundException(name));
    }

    @Override
    public Category createCategory(String name) {
        log.info("Creating new category: {}", name);
        if(categoryRepository.existsByName(name))
            throw new CategoryAlreadyExistsException(name);
        return categoryRepository.save(new Category(name));
    }
}
