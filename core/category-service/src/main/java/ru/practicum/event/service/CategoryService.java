package ru.practicum.event.service;

import ru.practicum.event.model.Category;

import java.util.List;

public interface CategoryService {
    Category addCategory(Category category);

    void deleteCategory(long catId);

    Category updateCategory(long catId, Category category);

    List<Category> getAllCategories(int from, int size);

    Category getCategory(long catId);
}
