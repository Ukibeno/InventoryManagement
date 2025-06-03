package InventoryManagement.repository.product;;


import InventoryManagement.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    @Query("SELECT p FROM Product p WHERE " +
            "(:category IS NULL OR p.category.name = :category) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Product> filterProducts(@Param("category") String category,
                                 @Param("minPrice") Double minPrice,
                                 @Param("maxPrice") Double maxPrice);

    List<Product> findByNameContainingIgnoreCase(String name);


    Optional<Product> findByProductCode(String productCode);
}
