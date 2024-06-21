package com.devtonin.hexarch.domain.mapper;

import com.devtonin.hexarch.domain.dto.OrderItemDto;
import com.devtonin.hexarch.domain.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItem dtoToOrderItem (OrderItemDto orderItemDto);

    OrderItemDto orderItemToDto (OrderItem orderItem);
}
