package com.devtonin.hexarch.domain.repository;

import com.devtonin.hexarch.domain.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

   void save (Order order);

   Optional<Order> findOrderById (UUID orderId);

}
