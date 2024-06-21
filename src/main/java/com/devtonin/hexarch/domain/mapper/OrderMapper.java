package com.devtonin.hexarch.domain.mapper;

import com.devtonin.hexarch.domain.dto.OrderDto;
import com.devtonin.hexarch.domain.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order mapDtoToOrder (OrderDto orderDto);

    OrderDto mapOrderToDto (Order order);
}
