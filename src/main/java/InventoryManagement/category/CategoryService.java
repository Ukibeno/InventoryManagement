package InventoryManagement.category;


import InventoryManagement.dto.CategoryCreationRequestDto;
import InventoryManagement.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();

    CategoryDto getCategoryById(Integer id);

    CategoryDto createCategory(CategoryCreationRequestDto categoryCreationRequestDto);

    CategoryDto updateCategory(Integer id, CategoryCreationRequestDto categoryCreationRequestDto);
    void deleteCategory(Integer id);
}
