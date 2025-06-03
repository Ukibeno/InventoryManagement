package InventoryManagement.category;

import InventoryManagement.dto.CategoryCreationRequestDto;
import InventoryManagement.dto.CategoryDto;
import InventoryManagement.category.CategoryService;
import InventoryManagement.dto.ApiSuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasAnyAuthority('ADMIN_READ', 'MANAGEMENT_READ')")
    @GetMapping
    public ResponseEntity<ApiSuccessResponse<List<CategoryDto>>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        ApiSuccessResponse<List<CategoryDto>> response = new ApiSuccessResponse<>(
                true,
                "Categories fetched successfully",
                categories,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_READ', 'MANAGEMENT_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<CategoryDto>> getCategoryById(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryById(id);
        ApiSuccessResponse<CategoryDto> response = new ApiSuccessResponse<>(
                true,
                "Category fetched successfully",
                category,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN_CREATE')")
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<CategoryDto>> createCategory(@Valid @RequestBody CategoryCreationRequestDto dto) {
        CategoryDto createdCategory = categoryService.createCategory(dto);
        ApiSuccessResponse<CategoryDto> response = new ApiSuccessResponse<>(
                true,
                "Category created successfully",
                createdCategory,
                HttpStatus.CREATED.value()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAuthority('ADMIN_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<CategoryDto>> updateCategory(@PathVariable Long id, @RequestBody CategoryCreationRequestDto dto) {
        CategoryDto updatedCategory = categoryService.updateCategory(id, dto);
        ApiSuccessResponse<CategoryDto> response = new ApiSuccessResponse<>(
                true,
                "Category updated successfully",
                updatedCategory,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        ApiSuccessResponse<Void> response = new ApiSuccessResponse<>(
                true,
                "Category deleted successfully",
                null,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }
}
