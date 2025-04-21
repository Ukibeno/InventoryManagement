package InventoryManagement.product.impl;


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
    private final ProductRepository adminProductRepository;
    private final CategoryRepository adminCategoryRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDto> getAllProducts() {
        return adminProductRepository.findAll().stream()
                .map(productMapper::productToDto)
                .collect(Collectors.toList());
    }
@Override
    public ProductDto createProduct(ProductDto productDTO) {
        if (productDTO.getCategoryDto() == null || productDTO.getCategoryDto().getId() == null) {
            throw new IllegalArgumentException("Product category must not be null");
        }

        Category existingCategory = adminCategoryRepository.findById(productDTO.getCategoryDto().getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = productMapper.productDtoToEntity(productDTO);
        product.setCategory(existingCategory);

        Product savedProduct = adminProductRepository.save(product);
        return productMapper.productToDto(savedProduct);
    }
@Override
    public ProductDto getProductById(Long id) {
        Product product = adminProductRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

        return productMapper.productToDto(product);
    }

    @Override
    public void deleteProduct(Long id) {
        adminProductRepository.deleteById(id);
    }
@Override
    public Optional<ProductDto> getProductByName(String name) {
        return adminProductRepository.findByName(name)
                .map(productMapper::productToDto);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDTO) {
        Product product = adminProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());

        if (productDTO.getCategoryDto() != null) {
            Category category = adminCategoryRepository.findById(productDTO.getCategoryDto().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        Product updatedProduct = adminProductRepository.save(product);
        return productMapper.productToDto(updatedProduct);
    }
    @Override
    public List<ProductDto> filterProducts(String category, Double minPrice, Double maxPrice, String sortBy, String order) {
        List<Product> products = adminProductRepository.filterProducts(category, minPrice, maxPrice);

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
        return adminProductRepository.findByNameContainingIgnoreCase(query).stream()
                .map(productMapper::productToDto)
                .collect(Collectors.toList());
    }
}
