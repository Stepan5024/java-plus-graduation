package ru.practicum.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.dto.UpdateCategoryDto;
import ru.practicum.category.dto.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;

/**
 * Controller for managing categories in the admin section.
 * Provides endpoints for adding, deleting, and updating categories.
 */
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryAdminController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    /**
     * Adds a new category based on the provided details.
     *
     * @param newCategoryDto the data transfer object containing details of the new category to add
     * @return the data transfer object representing the newly created category
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("Add category: {}", newCategoryDto);
        Category category = categoryMapper.toCategory(newCategoryDto);
        return categoryMapper.toCategoryDto(categoryService.addCategory(category));
    }

    /**
     * Deletes an existing category identified by its ID.
     *
     * @param catId the unique identifier of the category to be deleted
     */
    @DeleteMapping("/{cat-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("cat-id") long catId) {
        log.info("Delete category: {}", catId);
        categoryService.deleteCategory(catId);
    }

    /**
     * Updates an existing category identified by its ID with the provided details.
     *
     * @param catId the unique identifier of the category to update
     * @param dto the data transfer object containing the updated information for the category
     * @return the data transfer object representing the updated category
     */
    @PatchMapping("/{cat-id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PathVariable("cat-id") long catId, @RequestBody @Valid UpdateCategoryDto dto) {
        log.info("Update category: {}", dto);
        return categoryMapper.toCategoryDto(
                categoryService.updateCategory(catId, categoryMapper.fromUpdateCategoryDto(dto)));
    }
}
