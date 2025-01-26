package ru.practicum.exchange;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.dto.category.CategoryDto;

@FeignClient(name = "category-service")
public interface CategoryFeignClient {
    @GetMapping("/categories/{id}")
    CategoryDto getCategoryById(@PathVariable("id") Long id);
}