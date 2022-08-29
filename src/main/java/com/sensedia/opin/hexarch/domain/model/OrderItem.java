package com.sensedia.opin.hexarch.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderItem {
   private Product product;
   private Integer quantity;
}
