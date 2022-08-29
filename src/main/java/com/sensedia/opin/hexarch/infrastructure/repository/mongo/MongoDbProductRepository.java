package com.sensedia.opin.hexarch.infrastructure.repository.mongo;

import com.sensedia.opin.hexarch.domain.model.Product;
import com.sensedia.opin.hexarch.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class MongoDbProductRepository implements ProductRepository {

    private final SpringDataMongoProductRepository productRepository;

    @Autowired
    public MongoDbProductRepository(final SpringDataMongoProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public Optional<Product> findProductById(UUID productId) {
        return productRepository.findById(productId);
    }

    @Override
    public void delete(UUID productId) {
        productRepository.deleteById(productId);
    }
}
