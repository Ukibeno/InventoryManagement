package InventoryManagement.product;

import InventoryManagement.dto.ProductDto;
import InventoryManagement.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Get all products.
     * Accessible by both ADMIN and MANAGER roles.
     *
     * @return ResponseEntity containing the list of ProductDto.
     */
    @PreAuthorize("hasAnyAuthority('admin:read', 'management:read')")
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Create a new product.
     * Accessible only by ADMIN role.
     *
     * @param productDto the product data.
     * @return ResponseEntity containing the created ProductDto.
     */
    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto createdProduct = productService.createProduct(productDto);
        return ResponseEntity.ok(createdProduct);
    }

    /**
     * Get product by ID.
     * Accessible by both ADMIN and MANAGER roles.
     *
     * @param id the ID of the product.
     * @return ResponseEntity containing the ProductDto.
     */
    @PreAuthorize("hasAnyAuthority('admin:read', 'management:read')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Update an existing product.
     * Accessible only by ADMIN role.
     *
     * @param id the ID of the product to update.
     * @param productDto the updated product data.
     * @return ResponseEntity containing the updated ProductDto.
     */
    @PreAuthorize("hasAuthority('admin:update')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        ProductDto updatedProduct = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Delete a product by ID.
     * Accessible only by ADMIN role.
     *
     * @param id the ID of the product to delete.
     * @return ResponseEntity with no content.
     */
    @PreAuthorize("hasAuthority('admin:delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search for products by query.
     * Accessible by both ADMIN and MANAGER roles.
     *
     * @param query the search query.
     * @return ResponseEntity containing the list of ProductDto.
     */
    @PreAuthorize("hasAnyAuthority('admin:read', 'management:read')")
    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam String query) {
        List<ProductDto> products = productService.searchProducts(query);
        return ResponseEntity.ok(products);
    }

    /**
     * Filter products based on criteria.
     * Accessible by both ADMIN and MANAGER roles.
     *
     * @param category the category filter.
     * @param minPrice the minimum price filter.
     * @param maxPrice the maximum price filter.
     * @param sortBy the sorting criteria.
     * @param order the sorting order.
     * @return ResponseEntity containing the list of ProductDto.
     */
    @PreAuthorize("hasAnyAuthority('admin:read', 'management:read')")
    @GetMapping("/filter")
    public ResponseEntity<List<ProductDto>> filterProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order) {
        List<ProductDto> products = productService.filterProducts(category, minPrice, maxPrice, sortBy, order);
        return ResponseEntity.ok(products);
    }
}
