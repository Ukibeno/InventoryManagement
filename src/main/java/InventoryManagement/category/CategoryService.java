package InventoryManagement.category;


import InventoryManagement.dto.CategoryCreationRequestDto;
import InventoryManagement.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();

    CategoryDto getCategoryById(Long id);

    CategoryDto createCategory(CategoryCreationRequestDto categoryCreationRequestDto);

    CategoryDto updateCategory(Long id, CategoryCreationRequestDto categoryCreationRequestDto);
    void deleteCategory(Long id);
}
