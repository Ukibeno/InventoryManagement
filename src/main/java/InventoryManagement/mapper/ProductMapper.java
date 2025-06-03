package InventoryManagement.mapper;


import InventoryManagement.dto.ProductCreationRequestDto;
import InventoryManagement.dto.ProductDto;
import InventoryManagement.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

 // Map Product to ProductDto, category field mapped as is (Category -> CategoryDto assumed)
 @Mapping(source = "category", target = "category")
 ProductDto productToDto(Product product);

 // For creating/updating Product entity from ProductCreationRequestDto, ignore category here
 // because category needs to be set manually in service layer after fetching from DB
 Product productDtoToEntity(ProductCreationRequestDto productCreationRequestDto);
}

