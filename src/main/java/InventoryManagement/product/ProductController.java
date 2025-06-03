package InventoryManagement.product;

import InventoryManagement.dto.ApiSuccessResponse;
import InventoryManagement.dto.ProductCreationRequestDto;
import InventoryManagement.dto.ProductDto;
import InventoryManagement.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasAnyAuthority('ADMIN_READ', 'MANAGEMENT_READ')")
    @GetMapping
    public ResponseEntity<ApiSuccessResponse<List<ProductDto>>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        ApiSuccessResponse<List<ProductDto>> response = new ApiSuccessResponse<>(
                true,
                "Products fetched successfully",
                products,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN_CREATE')")
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<ProductDto>> createProduct(@Valid @RequestBody ProductCreationRequestDto productCreationRequestDto) {
        ProductDto createdProduct = productService.createProduct(productCreationRequestDto);
        ApiSuccessResponse<ProductDto> response = new ApiSuccessResponse<>(
                true,
                "Product created successfully",
                createdProduct,
                HttpStatus.CREATED.value()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_READ', 'MANAGEMENT_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<ProductDto>> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        ApiSuccessResponse<ProductDto> response = new ApiSuccessResponse<>(
                true,
                "Product fetched successfully",
                product,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<ProductDto>> updateProduct(@PathVariable Long id, @RequestBody ProductCreationRequestDto productCreationRequestDto) {
        ProductDto updatedProduct = productService.updateProduct(id, productCreationRequestDto);
        ApiSuccessResponse<ProductDto> response = new ApiSuccessResponse<>(
                true,
                "Product updated successfully",
                updatedProduct,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        ApiSuccessResponse<Void> response = new ApiSuccessResponse<>(
                true,
                "Product deleted successfully",
                null,
                HttpStatus.NO_CONTENT.value()
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_READ', 'MANAGEMENT_READ')")
    @GetMapping("/search")
    public ResponseEntity<ApiSuccessResponse<List<ProductDto>>> searchProducts(@RequestParam String query) {
        List<ProductDto> products = productService.searchProducts(query);
        ApiSuccessResponse<List<ProductDto>> response = new ApiSuccessResponse<>(
                true,
                "Products search results",
                products,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_READ', 'MANAGEMENT_READ')")
    @GetMapping("/filter")
    public ResponseEntity<ApiSuccessResponse<List<ProductDto>>> filterProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order) {
        List<ProductDto> products = productService.filterProducts(category, minPrice, maxPrice, sortBy, order);
        ApiSuccessResponse<List<ProductDto>> response = new ApiSuccessResponse<>(
                true,
                "Products filtered successfully",
                products,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }
}
