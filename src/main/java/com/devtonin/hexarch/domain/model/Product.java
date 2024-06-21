package com.devtonin.hexarch.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Data
@Document
@Setter
@Getter
public class Product {

   @Id
   private UUID productId;
   private String name;
   private BigDecimal price;
}
