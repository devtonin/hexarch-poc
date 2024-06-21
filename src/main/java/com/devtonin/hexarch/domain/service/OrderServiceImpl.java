package com.devtonin.hexarch.domain.service;

import com.devtonin.hexarch.domain.dto.OrderDto;
import com.devtonin.hexarch.domain.dto.OrderItemDto;
import com.devtonin.hexarch.domain.mapper.OrderMapper;
import com.devtonin.hexarch.domain.model.Order;
import com.devtonin.hexarch.domain.dto.OrderStatusDto;
import com.devtonin.hexarch.domain.exceptions.DomainException;
import com.devtonin.hexarch.domain.messaging.OrderProducer;
import com.devtonin.hexarch.domain.exceptions.ErrorResponse;
import com.devtonin.hexarch.domain.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

   private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

   private final OrderRepository orderRepository;

   private final OrderProducer orderProducer;

   private final ProductServiceImpl productServiceImpl;

   private final OrderMapper orderMapper;

   public OrderServiceImpl (OrderRepository orderRepository, OrderProducer orderProducer, ProductServiceImpl productServiceImpl, OrderMapper orderMapper) {
      this.orderRepository = orderRepository;
      this.orderProducer = orderProducer;
      this.productServiceImpl = productServiceImpl;
      this.orderMapper = orderMapper;
   }

   @Override
   public OrderDto createOrder (List<OrderItemDto> orderItems) {
      checkIfProductExists(orderItems);
      OrderDto orderDto = OrderDto.builder()
              .orderId(UUID.randomUUID())
              .orderItems(orderItems)
              .orderStatusDto(OrderStatusDto.COMPLETED)
              .totalPrice(calculatePrice(orderItems))
              .build();
      Order order = orderMapper.mapDtoToOrder(orderDto);
      orderRepository.save(order);
      LOGGER.info("Created order {}.", order.getOrderId());
      orderProducer.send(orderDto);
      LOGGER.info("Sent order {} to preparation.", orderDto.getOrderId());
      return orderDto;
   }

   private void checkIfProductExists (List<OrderItemDto> orderItems) {
      LOGGER.info("Validating products existance.");
      for (OrderItemDto item: orderItems) {
         productServiceImpl.findProductById(item.getProduct().getProductId());
      }
   }

   private BigDecimal calculatePrice (List<OrderItemDto> orderItems) {
      BigDecimal totalPrice = BigDecimal.ZERO;
      for (OrderItemDto item : orderItems) {
         var productPrice = item.getProduct().getPrice();
         var productQuantity = item.getQuantity();
         totalPrice = totalPrice.add(productPrice.multiply(BigDecimal.valueOf(productQuantity)));
      }
      return totalPrice;
   }

   @Override
   public OrderDto cancelOrder (UUID orderId) {
      LOGGER.info("Verify if order {} exists.", orderId);
      Order order = orderRepository.findOrderById(orderId)
              .orElseThrow(() -> new DomainException(List.of(new ErrorResponse("TPL-1000",
                                                                        "Pedido não encontrado",
                                                                        "Verifique se o id informado está correto."))));
      LOGGER.info("Set order {} to cancelled.", orderId);
      order.setOrderStatusDto(OrderStatusDto.CANCELLED);
      orderRepository.save(order);
      LOGGER.info("Updated order {} into database.", orderId);
      OrderDto orderDto = orderMapper.mapOrderToDto(order);
      orderProducer.send(orderDto);
      LOGGER.info("Sent updated order {} to preparation.", orderId);
      return orderDto;
   }

   @Override
   public OrderDto findOrderById (UUID orderId) {
      LOGGER.info("Verify if order {} exists.", orderId);
      Order order = orderRepository.findOrderById(orderId)
              .orElseThrow(() -> new DomainException(List.of(new ErrorResponse("TPL-1000",
                                                                         "Pedido não encontrado",
                                                                         "Verifique se o id informado está correto."))));
      return orderMapper.mapOrderToDto(order);
   }
}
