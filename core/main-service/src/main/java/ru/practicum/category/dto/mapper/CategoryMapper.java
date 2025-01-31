package ru.practicum.category.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.dto.UpdateCategoryDto;
import ru.practicum.category.model.Category;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    Category toCategory(NewCategoryDto newCategoryDto);

    CategoryDto toCategoryDto(Category category);

    Category fromUpdateCategoryDto(UpdateCategoryDto updateCategoryDto);

    List<CategoryDto> toCategoryDtoList(List<Category> categoryList);
}
