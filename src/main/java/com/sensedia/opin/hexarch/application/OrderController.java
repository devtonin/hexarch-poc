package com.sensedia.opin.hexarch.application;

import com.sensedia.opin.hexarch.domain.dto.OrderDto;
import com.sensedia.opin.hexarch.domain.dto.OrderItemDto;
import com.sensedia.opin.hexarch.domain.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController (OrderService orderService) { this.orderService = orderService; }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<OrderDto> createOrder (@RequestBody final List<OrderItemDto> orderItems) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderItems));
    }

    @GetMapping(value = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<OrderDto> findOrderById (@PathVariable final UUID orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findOrderById(orderId));
    }

    @PatchMapping(value = "/{orderId}/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<OrderDto> cancelOrder (@PathVariable final UUID orderId){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.cancelOrder(orderId));
    }
}
