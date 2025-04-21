package InventoryManagement.mapper;

import InventoryManagement.dto.CategoryDto;
import InventoryManagement.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);
    Category toEntity(CategoryDto categoryDto);
}
