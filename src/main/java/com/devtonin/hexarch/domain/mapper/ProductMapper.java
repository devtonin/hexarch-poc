package com.devtonin.hexarch.domain.mapper;

import com.devtonin.hexarch.domain.dto.ProductDto;
import com.devtonin.hexarch.domain.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product mapDtoToProduct (ProductDto productDto);

    ProductDto mapProductToDto (Product product);
}
