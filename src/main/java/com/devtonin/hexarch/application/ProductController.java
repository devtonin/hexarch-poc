package com.devtonin.hexarch.application;

import com.devtonin.hexarch.domain.dto.ProductDto;
import com.devtonin.hexarch.domain.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

   private final ProductService productService;

   @Autowired
   public ProductController(ProductService productService) {
      this.productService = productService;
   }

   @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
   private ResponseEntity<ProductDto> createProduct (@RequestBody final ProductDto product){
      return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
   }

   @GetMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
   private ResponseEntity<ProductDto> findProductById (@PathVariable final UUID productId){
      return ResponseEntity.status(HttpStatus.OK).body(productService.findProductById(productId));
   }

   @DeleteMapping(value = "/{productId}")
   private ResponseEntity<?> deleteProduct (@PathVariable final UUID productId){
      productService.deleteProduct(productId);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
   }
}
