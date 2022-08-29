package com.sensedia.opin.hexarch.domain.repository;

import com.sensedia.opin.hexarch.domain.model.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
   void save (Product product);

   Optional<Product> findProductById (UUID productId);

   void delete (UUID productId);
}
