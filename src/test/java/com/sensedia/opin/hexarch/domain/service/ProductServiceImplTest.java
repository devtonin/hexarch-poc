package com.sensedia.opin.hexarch.domain.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.sensedia.opin.hexarch.domain.dto.ProductDto;
import com.sensedia.opin.hexarch.domain.mapper.ProductMapper;
import com.sensedia.opin.hexarch.domain.model.Product;
import com.sensedia.opin.hexarch.domain.exceptions.DomainException;
import com.sensedia.opin.hexarch.domain.exceptions.ErrorResponse;
import com.sensedia.opin.hexarch.domain.repository.ProductRepository;
import com.sensedia.opin.hexarch.utils.TestCustomAppender;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

   @Mock
   private ProductRepository productRepository;

   @Mock
   private ProductMapper productMapper;
   private static TestCustomAppender listAppender;
   @InjectMocks
   private ProductServiceImpl productServiceImpl;
   private final UUID productId = UUID.randomUUID();
   private final ProductDto productDto = ProductDto.builder().name("Refrigerante").price(BigDecimal.valueOf(5)).build();
   private final Product product = Product.builder().name("Refrigerante").price(BigDecimal.valueOf(5)).build();

   private final ErrorResponse errorResponse = new ErrorResponse("TPL-1001", "Produto não encontrado", "Verifique se o id do produto informado está correto.");
   private final DomainException domainException = new DomainException(List.of(errorResponse));

   @BeforeAll
   static void setup () {
      Logger LOGGER = (Logger) LoggerFactory.getLogger(ProductServiceImpl.class);
      listAppender = new TestCustomAppender();
      LOGGER.addAppender(listAppender);
      listAppender.start();
   }

   @AfterEach
   void teardown () {
      listAppender.clear();
   }

   @Test
   void shouldCreateProduct () {
      //arrange
      when(productMapper.mapDtoToProduct(productDto)).thenReturn(product);

      //act
      ProductDto actualProduct = productServiceImpl.createProduct(productDto);

      //assert
      verify(productRepository, times(1)).save(product);
      assertNotNull(actualProduct.getProductId());
      assertEquals(productDto, actualProduct);
      assertThat(listAppender.list)
              .extracting(ILoggingEvent::getFormattedMessage, ILoggingEvent::getLevel)
              .containsExactly(Tuple.tuple(String.format("Created product %s.", product.getProductId()), Level.INFO));
   }

   @Test
   void shouldDeleteProduct () {
      //act and assert
      productServiceImpl.deleteProduct(productId);
      verify(productRepository, times(1)).delete(productId);
      assertThat(listAppender.list)
              .extracting(ILoggingEvent::getFormattedMessage, ILoggingEvent::getLevel)
              .containsExactly(Tuple.tuple(String.format("Try to delete product %s.",productId), Level.INFO));
   }

   @Test
   void shouldFindProductById () {
      //arrange
      when(productRepository.findProductById(productId)).thenReturn(Optional.ofNullable(product));
      when(productMapper.mapProductToDto(product)).thenReturn(productDto);

      //act
      ProductDto actualProduct = productServiceImpl.findProductById(productId);

      //assert
      assertEquals(productDto, actualProduct);
      assertThat(listAppender.list)
              .extracting(ILoggingEvent::getFormattedMessage, ILoggingEvent::getLevel)
              .containsExactly(Tuple.tuple(String.format("Verify if product %s exists.", productId), Level.INFO));
   }

   @Test
   void shouldNotFindProductById () {
      //arrange
      when(productRepository.findProductById(productId)).thenThrow(domainException);

      //act
      DomainException actualDomainException = assertThrows(DomainException.class, () -> productServiceImpl.findProductById(productId));

      //assert
      assertEquals(domainException, actualDomainException);
      verify(productMapper, never()).mapProductToDto(product);
      assertThat(actualDomainException)
              .usingRecursiveComparison()
              .ignoringFieldsOfTypes(Instant.class)
              .isEqualTo(domainException);
      assertThat(listAppender.list)
              .extracting(ILoggingEvent::getFormattedMessage, ILoggingEvent::getLevel)
              .containsExactly(Tuple.tuple(String.format("Verify if product %s exists.", productId), Level.INFO));
   }

}
