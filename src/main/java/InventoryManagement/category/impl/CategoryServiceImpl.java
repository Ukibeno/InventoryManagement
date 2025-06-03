package InventoryManagement.category.impl;


import InventoryManagement.dto.CategoryCreationRequestDto;
import InventoryManagement.repository.category.CategoryRepository;
import InventoryManagement.category.CategoryService;
import InventoryManagement.dto.CategoryDto;
import InventoryManagement.mapper.CategoryMapper;
import InventoryManagement.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found for ID: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto createCategory(CategoryCreationRequestDto categoryCreationRequestDto) {
        Category category = categoryMapper.toEntity(categoryCreationRequestDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryCreationRequestDto categoryCreationRequestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(categoryCreationRequestDto.getName());
        category.setDescription(categoryCreationRequestDto.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    }


