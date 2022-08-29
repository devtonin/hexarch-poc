package com.sensedia.opin.hexarch.domain.mapper;

import com.sensedia.opin.hexarch.domain.dto.OrderItemDto;
import com.sensedia.opin.hexarch.domain.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItem dtoToOrderItem (OrderItemDto orderItemDto);

    OrderItemDto orderItemToDto (OrderItem orderItem);
}
