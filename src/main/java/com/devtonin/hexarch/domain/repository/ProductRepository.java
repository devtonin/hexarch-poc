package com.devtonin.hexarch.domain.repository;

import com.devtonin.hexarch.domain.model.Product;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
   void save (Product product);

   Optional<Product> findProductById (UUID productId);

   void delete (UUID productId);
}
