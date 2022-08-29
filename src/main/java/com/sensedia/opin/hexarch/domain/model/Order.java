package com.sensedia.opin.hexarch.domain.model;

import com.sensedia.opin.hexarch.domain.dto.OrderStatusDto;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@Document
@Setter
@Getter
public class Order {

   @Id
   private UUID orderId;
   private OrderStatusDto orderStatusDto = OrderStatusDto.CREATED;
   private List<OrderItem> orderItems;
   private BigDecimal totalPrice;
}
