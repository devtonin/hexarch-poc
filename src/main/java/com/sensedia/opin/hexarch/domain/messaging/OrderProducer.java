package com.sensedia.opin.hexarch.domain.messaging;

import com.sensedia.opin.hexarch.domain.dto.OrderDto;

public interface OrderProducer {
   void send(OrderDto orderResponse);
}
