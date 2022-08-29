package com.sensedia.opin.hexarch.infrastructure.repository.mongo;

import com.sensedia.opin.hexarch.domain.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataMongoProductRepository extends MongoRepository<Product, UUID> {
}
