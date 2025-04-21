package InventoryManagement.product;

import InventoryManagement.dto.ProductCreationRequestDto;
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


    @PreAuthorize("hasAnyAuthority('admin:read', 'management:read')")
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductCreationRequestDto productCreationRequestDto) {
        ProductDto createdProduct = productService.createProduct(productCreationRequestDto);
        return ResponseEntity.ok(createdProduct);
    }

    @PreAuthorize("hasAnyAuthority('admin:read', 'management:read')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PreAuthorize("hasAuthority('admin:update')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductCreationRequestDto productCreationRequestDto) {
        ProductDto updatedProduct = productService.updateProduct(id, productCreationRequestDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @PreAuthorize("hasAuthority('admin:delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAnyAuthority('admin:read', 'management:read')")
    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam String query) {
        List<ProductDto> products = productService.searchProducts(query);
        return ResponseEntity.ok(products);
    }

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
