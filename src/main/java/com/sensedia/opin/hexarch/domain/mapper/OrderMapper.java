package com.sensedia.opin.hexarch.domain.mapper;

import com.sensedia.opin.hexarch.domain.dto.OrderDto;
import com.sensedia.opin.hexarch.domain.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order mapDtoToOrder (OrderDto orderDto);

    OrderDto mapOrderToDto (Order order);
}
