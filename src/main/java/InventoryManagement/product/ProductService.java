package InventoryManagement.product;



import InventoryManagement.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto createProduct(ProductDto productDTO);

    ProductDto getProductById(Long id);

  void deleteProduct(Long id);

    Optional<ProductDto> getProductByName(String name);
    ProductDto updateProduct(Long id, ProductDto productDTO);
    List<ProductDto> filterProducts(String category, Double minPrice, Double maxPrice, String sortBy, String order);
    List<ProductDto> searchProducts(String query);


}
