package com.devtonin.hexarch.domain.messaging;

import com.devtonin.hexarch.domain.dto.OrderDto;

public interface OrderProducer {
   void send(OrderDto orderResponse);
}
