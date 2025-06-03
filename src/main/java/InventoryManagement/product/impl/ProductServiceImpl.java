package InventoryManagement.product.impl;

import InventoryManagement.dto.ProductCreationRequestDto;
import InventoryManagement.dto.ProductDto;
import InventoryManagement.exception.ProductNotFoundException;
import InventoryManagement.mapper.ProductMapper;
import InventoryManagement.model.Category;
import InventoryManagement.model.Product;
import InventoryManagement.repository.category.CategoryRepository;
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
    public ProductDto createProduct(ProductCreationRequestDto request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + request.getCategoryId()));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .productCode(request.getProductCode())
                .category(category)
                .build();

        return productMapper.productToDto(productRepository.save(product));
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

        return productMapper.productToDto(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public Optional<ProductDto> getProductByName(String name) {
        return productRepository.findByName(name)
                .map(productMapper::productToDto);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductCreationRequestDto request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategory(category);

        return productMapper.productToDto(productRepository.save(product));
    }

    @Override
    public List<ProductDto> filterProducts(String category, Double minPrice, Double maxPrice, String sortBy, String order) {
        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice);

        return products.stream()
                .sorted((p1, p2) -> {
                    if (sortBy == null) return 0;

                    if ("price".equalsIgnoreCase(sortBy)) {
                        BigDecimal price1 = Optional.ofNullable(p1.getPrice()).orElse(BigDecimal.ZERO);
                        BigDecimal price2 = Optional.ofNullable(p2.getPrice()).orElse(BigDecimal.ZERO);
                        return "desc".equalsIgnoreCase(order) ? price2.compareTo(price1) : price1.compareTo(price2);
                    }

                    if ("name".equalsIgnoreCase(sortBy)) {
                        return "desc".equalsIgnoreCase(order)
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
