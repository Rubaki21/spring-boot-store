package ru.technical.store.mapper;

import ru.technical.store.dto.ProductDto;
import ru.technical.store.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import store.ProductAvroDto;

@Mapper
public interface ProductMapper {

  @Mapping(target = "category.categoryName", source = "categoryName")
  Product convertProductFromAvroDto(ProductAvroDto product);

  @Mapping(target = "categoryName", source = "category.categoryName")
  ProductDto convertProductDtoFromProduct(Product product);
}
