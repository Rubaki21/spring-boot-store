package ru.technical.store.mapper;

import ru.technical.store.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import store.ProductDto;

@Mapper
public interface ProductMapper {

  @Mapping(target = "category.categoryName", source = "categoryName")
  Product convertProductFromDto(ProductDto product);
}
