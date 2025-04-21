package InventoryManagement.category;

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

    /**
     * Get all categories.
     * Accessible by both ADMIN and MANAGER roles.
     *
     * @return ResponseEntity containing the list of CategoryDto.
     */
    @PreAuthorize("hasAnyAuthority('admin:read', 'management:read')")
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * Get category by ID.
     * Accessible by both ADMIN and MANAGER roles.
     *
     * @param id the ID of the category.
     * @return ResponseEntity containing the CategoryDto.
     */
    @PreAuthorize("hasAnyAuthority('admin:read', 'management:read')")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Integer id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * Create a new category.
     * Accessible only by ADMIN role.
     *
     * @param categoryDto the category data.
     * @return ResponseEntity containing the created CategoryDto.
     */
    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(201).body(createdCategory);
    }

    /**
     * Update an existing category.
     * Accessible only by ADMIN role.
     *
     * @param id the ID of the category to update.
     * @param categoryDto the updated category data.
     * @return ResponseEntity containing the updated CategoryDto.
     */
    @PreAuthorize("hasAuthority('admin:update')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Integer id, @RequestBody CategoryDto categoryDto) {
        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
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
