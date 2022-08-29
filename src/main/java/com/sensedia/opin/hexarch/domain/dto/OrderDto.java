package com.sensedia.opin.hexarch.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private UUID orderId;

    @Builder.Default
    private OrderStatusDto orderStatusDto = OrderStatusDto.CREATED;

    @NonNull
    private List<OrderItemDto> orderItems;

    @NonNull
    private BigDecimal totalPrice;
}
