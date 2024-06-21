package com.devtonin.hexarch.domain.service;

import com.devtonin.hexarch.domain.dto.ProductDto;

import java.util.UUID;
public interface ProductService {

   ProductDto createProduct (ProductDto product);

   ProductDto findProductById (UUID productId);

   void deleteProduct (UUID productId);
}
