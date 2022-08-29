package com.sensedia.opin.hexarch.domain.service;

import com.sensedia.opin.hexarch.domain.dto.ProductDto;
import com.sensedia.opin.hexarch.domain.mapper.ProductMapper;
import com.sensedia.opin.hexarch.domain.model.Product;
import com.sensedia.opin.hexarch.domain.exceptions.DomainException;
import com.sensedia.opin.hexarch.domain.exceptions.ErrorResponse;
import com.sensedia.opin.hexarch.domain.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

   private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
   private final ProductRepository productRepository;
   private final ProductMapper productMapper;

   public ProductServiceImpl (ProductRepository productRepository, ProductMapper productMapper) {
      this.productRepository = productRepository;
      this.productMapper = productMapper;
   }

   @Override
   public ProductDto createProduct (ProductDto productDto) {
      productDto.setProductId(UUID.randomUUID());
      Product product = productMapper.mapDtoToProduct(productDto);
      LOGGER.info("Created product {}.", product.getProductId());
      productRepository.save(product);
      return productDto;
   }

   @Override
   public ProductDto findProductById (UUID productId) {
      LOGGER.info("Verify if product {} exists.", productId);
      Product product = productRepository.findProductById(productId)
              .orElseThrow(() -> new DomainException(List.of(new ErrorResponse("TPL-1001",
                      "Produto não encontrado",
                      "Verifique se o id do produto informado está correto."))));
      return productMapper.mapProductToDto(product);
   }

   @Override
   public void deleteProduct (UUID productId) {
      LOGGER.info("Try to delete product {}.", productId);
      productRepository.delete(productId);
   }
}
