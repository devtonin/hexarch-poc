package com.sensedia.opin.hexarch.infrastructure.repository.mongo;

import com.sensedia.opin.hexarch.domain.model.Order;
import com.sensedia.opin.hexarch.domain.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class MongoDbOrderRepository implements OrderRepository {

    private final SpringDataMongoOrderRepository orderRepository;

    @Autowired
    public MongoDbOrderRepository(final SpringDataMongoOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public Optional<Order> findOrderById(UUID orderId) {
        return orderRepository.findById(orderId);
    }

}
