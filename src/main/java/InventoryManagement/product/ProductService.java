package InventoryManagement.product;



import InventoryManagement.dto.ProductCreationRequestDto;
import InventoryManagement.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto createProduct(ProductCreationRequestDto productCreationRequestDto);

    ProductDto getProductById(Long id);

  void deleteProduct(Long id);

    Optional<ProductDto> getProductByName(String name);
    ProductDto updateProduct(Long id, ProductCreationRequestDto productCreationRequestDto);
    List<ProductDto> filterProducts(String category, Double minPrice, Double maxPrice, String sortBy, String order);
    List<ProductDto> searchProducts(String query);


}
