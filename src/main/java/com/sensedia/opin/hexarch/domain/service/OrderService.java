package com.sensedia.opin.hexarch.domain.service;

import com.sensedia.opin.hexarch.domain.dto.OrderDto;
import com.sensedia.opin.hexarch.domain.dto.OrderItemDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {

   OrderDto createOrder (List<OrderItemDto> orderItems);

   OrderDto cancelOrder (UUID orderId);

   OrderDto findOrderById (UUID orderId);
}
