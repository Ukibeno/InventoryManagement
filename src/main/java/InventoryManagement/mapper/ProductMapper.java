package InventoryManagement.mapper;


import InventoryManagement.dto.ProductDto;
import InventoryManagement.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {
 @Mapping(source = "category", target = "categoryDto")
 ProductDto productToDto(Product product);

 @Mapping(source = "categoryDto", target = "category")
 Product productDtoToEntity(ProductDto productDto);

}
