package ru.practicum.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.dto.UpdateCategoryDto;
import ru.practicum.model.Category;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    Category toCategory(NewCategoryDto newCategoryDto);

    CategoryDto toCategoryDto(Category category);

    Category fromUpdateCategoryDto(UpdateCategoryDto updateCategoryDto);

    List<CategoryDto> toCategoryDtoList(List<Category> categoryList);
}
