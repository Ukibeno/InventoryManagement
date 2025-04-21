package InventoryManagement.category;

import InventoryManagement.dto.CategoryCreationRequestDto;
import InventoryManagement.dto.CategoryDto;
import InventoryManagement.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasAnyAuthority('admin:read', 'management:read')")
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PreAuthorize("hasAnyAuthority('admin:read', 'management:read')")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Integer id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryCreationRequestDto categoryCreationRequestDto) {
        CategoryDto createdCategory = categoryService.createCategory(categoryCreationRequestDto);
        return ResponseEntity.status(201).body(createdCategory);
    }


    @PreAuthorize("hasAuthority('admin:update')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Integer id, @RequestBody CategoryCreationRequestDto categoryCreationRequestDto) {
        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryCreationRequestDto);
        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * Delete a category by ID.
     * Accessible only by ADMIN role.
     *
     * @param id the ID of the category to delete.
     * @return ResponseEntity with a success message.
     */
    @PreAuthorize("hasAuthority('admin:delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully.");
    }
}
