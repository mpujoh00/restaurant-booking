package com.restaurant.booking.restaurant.service.service;

import com.restaurant.booking.restaurant.model.Category;

import java.util.List;

public interface CategoryService {

    public List<Category> getAllCategories();

    public Category getCategory(String name);

    public Category createCategory(String name);
}
