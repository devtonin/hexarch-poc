package com.sensedia.opin.hexarch.domain.mapper;

import com.sensedia.opin.hexarch.domain.dto.ProductDto;
import com.sensedia.opin.hexarch.domain.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product mapDtoToProduct (ProductDto productDto);

    ProductDto mapProductToDto (Product product);
}
