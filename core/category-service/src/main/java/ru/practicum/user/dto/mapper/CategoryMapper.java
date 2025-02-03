package ru.practicum.user.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.user.dto.CategoryDto;
import ru.practicum.user.dto.NewCategoryDto;
import ru.practicum.user.dto.UpdateCategoryDto;
import ru.practicum.event.model.Category;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    Category toCategory(NewCategoryDto newCategoryDto);

    CategoryDto toCategoryDto(Category category);

    Category fromUpdateCategoryDto(UpdateCategoryDto updateCategoryDto);

    List<CategoryDto> toCategoryDtoList(List<Category> categoryList);
}
