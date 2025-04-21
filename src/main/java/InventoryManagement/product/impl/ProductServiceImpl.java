package InventoryManagement.product.impl;


import InventoryManagement.dto.ProductCreationRequestDto;
import InventoryManagement.repository.category.CategoryRepository;
import InventoryManagement.dto.ProductDto;
import InventoryManagement.exception.ProductNotFoundException;
import InventoryManagement.mapper.ProductMapper;
import InventoryManagement.model.Category;
import InventoryManagement.model.Product;
import InventoryManagement.repository.product.ProductRepository;
import InventoryManagement.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::productToDto)
                .collect(Collectors.toList());
    }
    @Override
    public ProductDto createProduct(ProductCreationRequestDto productCreationRequestDto) {
        if (productCreationRequestDto.getCategoryDto() == null ||
                productCreationRequestDto.getCategoryDto().getName() == null ||
                productCreationRequestDto.getCategoryDto().getName().isBlank()) {
            throw new IllegalArgumentException("Product category name must not be null or blank");
        }

        Category existingCategory = categoryRepository.findByName(productCreationRequestDto.getCategoryDto().getName())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = productMapper.productDtoToEntity(productCreationRequestDto);
        product.setCategory(existingCategory);

        Product savedProduct = productRepository.save(product);
        return productMapper.productToDto(savedProduct);
    }

@Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

        return productMapper.productToDto(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
@Override
    public Optional<ProductDto> getProductByName(String name) {
        return productRepository.findByName(name)
                .map(productMapper::productToDto);
    }

    @Override
    public ProductDto updateProduct(Long productId, ProductCreationRequestDto productCreationRequestDto) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // If categoryDto or name inside it is missing, throw error
        if (productCreationRequestDto.getCategoryDto() == null ||
                productCreationRequestDto.getCategoryDto().getName() == null ||
                productCreationRequestDto.getCategoryDto().getName().isBlank()) {
            throw new IllegalArgumentException("Product category name must not be null or blank");
        }

        // Find category by name
        Category existingCategory = categoryRepository.findByName(productCreationRequestDto.getCategoryDto().getName())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Update fields
        existingProduct.setName(productCreationRequestDto.getName());
        existingProduct.setDescription(productCreationRequestDto.getDescription());
        existingProduct.setPrice(productCreationRequestDto.getPrice());
        existingProduct.setCategory(existingCategory);
        existingProduct.setQuantity(productCreationRequestDto.getQuantity());
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.productToDto(updatedProduct);
    }

    @Override
    public List<ProductDto> filterProducts(String category, Double minPrice, Double maxPrice, String sortBy, String order) {
        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice);

        return products.stream()
                .sorted((p1, p2) -> {
                    if (sortBy == null) return 0;

                    if (sortBy.equalsIgnoreCase("price")) {
                        BigDecimal price1 = p1.getPrice() != null ? p1.getPrice() : BigDecimal.ZERO;
                        BigDecimal price2 = p2.getPrice() != null ? p2.getPrice() : BigDecimal.ZERO;
                        return order.equalsIgnoreCase("desc") ? price2.compareTo(price1) : price1.compareTo(price2);
                    }

                    if (sortBy.equalsIgnoreCase("name")) {
                        return order.equalsIgnoreCase("desc")
                                ? p2.getName().compareToIgnoreCase(p1.getName())
                                : p1.getName().compareToIgnoreCase(p2.getName());
                    }

                    return 0;
    })
                .map(productMapper::productToDto)
                .collect(Collectors.toList());
    }

@Override
    public List<ProductDto> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query).stream()
                .map(productMapper::productToDto)
                .collect(Collectors.toList());
    }
}
