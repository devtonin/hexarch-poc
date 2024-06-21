package com.devtonin.hexarch.domain.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.devtonin.hexarch.domain.dto.OrderDto;
import com.devtonin.hexarch.domain.dto.OrderItemDto;
import com.devtonin.hexarch.domain.dto.OrderStatusDto;
import com.devtonin.hexarch.domain.dto.ProductDto;
import com.devtonin.hexarch.domain.mapper.OrderMapper;
import com.devtonin.hexarch.domain.model.Order;
import com.devtonin.hexarch.domain.model.OrderItem;
import com.devtonin.hexarch.domain.model.Product;
import com.devtonin.hexarch.domain.exceptions.DomainException;
import com.devtonin.hexarch.domain.exceptions.ErrorResponse;
import com.devtonin.hexarch.domain.messaging.OrderProducer;
import com.devtonin.hexarch.domain.repository.OrderRepository;
import com.devtonin.hexarch.utils.TestCustomAppender;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

   @Mock
   private OrderRepository orderRepository;

   @Mock
   private OrderMapper orderMapper;

   @Mock
   private OrderProducer orderProducer;

   private static TestCustomAppender listAppender;

   @InjectMocks
   private OrderServiceImpl orderServiceImpl;

   @Mock
   private ProductServiceImpl productServiceImpl;

   private final UUID id1 = UUID.randomUUID();
   private final UUID id2 = UUID.randomUUID();
   private final ErrorResponse errorResponse = new ErrorResponse("TPL-1000", "Pedido não encontrado", "Verifique se o id informado está correto.");
   private final DomainException domainException = new DomainException(List.of(errorResponse));

   private final List<OrderItem> orderItems = List.of(
           OrderItem.builder()
                   .product(Product.builder().name("coffee").productId(id1).price(BigDecimal.valueOf(20)).build())
                   .quantity(2).build(),
           OrderItem.builder()
                   .product(Product.builder().name("cookie").productId(id2).price(BigDecimal.valueOf(6)).build())
                   .quantity(3).build());

   private final List<OrderItemDto> orderItemDto = List.of(
           OrderItemDto.builder()
                   .product(ProductDto.builder().name("coffee").productId(id1).price(BigDecimal.valueOf(20)).build())
                   .quantity(2).build(),
           OrderItemDto.builder()
                   .product(ProductDto.builder().name("cookie").productId(id2).price(BigDecimal.valueOf(6)).build())
                   .quantity(3).build());

   private final Order completedOrder = Order.builder().
           orderItems(orderItems)
           .totalPrice(BigDecimal.valueOf(58))
           .orderStatusDto(OrderStatusDto.COMPLETED)
           .build();

   private final OrderDto completedOrderDto = OrderDto.builder().
           orderItems(orderItemDto)
           .totalPrice(BigDecimal.valueOf(58))
           .orderStatusDto(OrderStatusDto.COMPLETED)
           .build();

   private final Order cancelledOrder = Order.builder()
           .orderItems(orderItems)
           .totalPrice(BigDecimal.valueOf(58))
           .orderStatusDto(OrderStatusDto.CANCELLED)
           .build();

   private final OrderDto cancelledOrderDto = OrderDto.builder()
           .orderItems(orderItemDto)
           .totalPrice(BigDecimal.valueOf(58))
           .orderStatusDto(OrderStatusDto.CANCELLED)
           .build();

   @BeforeAll
   static void setup () {
      Logger LOGGER = (Logger) LoggerFactory.getLogger(OrderServiceImpl.class);
      listAppender = new TestCustomAppender();
      LOGGER.addAppender(listAppender);
      listAppender.start();
   }

   @AfterEach
   void teardown () {
      listAppender.clear();
   }

   @Test
   void shouldCreateOrderWithTotalPriceOfTwoItems () {
      //arrange
      when(orderMapper.mapDtoToOrder(Mockito.any(OrderDto.class))).thenReturn(completedOrder);

      //act
      OrderDto actualOrder = orderServiceImpl.createOrder(orderItemDto);

      //assert
      verify(orderRepository, times(1)).save(completedOrder);
      verify(orderProducer, times(1)).send(actualOrder);
      assertNotNull(actualOrder.getOrderId());
      assertThat(actualOrder).usingRecursiveComparison().ignoringFields("orderId").isEqualTo(completedOrder);
      assertThat(listAppender.list)
              .extracting(ILoggingEvent::getFormattedMessage, ILoggingEvent::getLevel)
              .containsExactly(Tuple.tuple("Validating products existance.", Level.INFO),
                      Tuple.tuple(String.format("Created order %s.", completedOrder.getOrderId()), Level.INFO),
                      Tuple.tuple(String.format("Sent order %s to preparation.", actualOrder.getOrderId()), Level.INFO));
   }

   @Test
   void shouldNotCreateOrderWhenProductDoesntExists () {
      //arrange
      when(productServiceImpl.findProductById(id1)).thenThrow(domainException);

      //act and assert
      DomainException actualDomainException = assertThrows(DomainException.class, () -> orderServiceImpl.createOrder(orderItemDto));
      verify(orderRepository, never()).save(Mockito.any(Order.class));
      assertThat(listAppender.list)
              .extracting(ILoggingEvent::getFormattedMessage, ILoggingEvent::getLevel)
              .containsExactly(Tuple.tuple("Validating products existance.", Level.INFO));
   }

   @Test
   void shouldNotCreateOrderWhenTheSecondProductDoesntExists () {
      //arrange
      when(productServiceImpl.findProductById(id1)).thenReturn(orderItemDto.get(0).getProduct());
      when(productServiceImpl.findProductById(id2)).thenThrow(domainException);

      //act and assert
      DomainException actualDomainException = assertThrows(DomainException.class, () -> orderServiceImpl.createOrder(orderItemDto));
      verify(orderRepository, never()).save(Mockito.any(Order.class));
      assertThat(listAppender.list)
              .extracting(ILoggingEvent::getFormattedMessage, ILoggingEvent::getLevel)
              .containsExactly(Tuple.tuple("Validating products existance.", Level.INFO));
   }

   @Test
   void shouldFindOrderById () {
      //arrange
      when(orderRepository.findOrderById(id1)).thenReturn(Optional.ofNullable(completedOrder));
      when(orderMapper.mapOrderToDto(completedOrder)).thenReturn(completedOrderDto);

      //act
      OrderDto actualOrder = orderServiceImpl.findOrderById(id1);

      //assert
      assertEquals(completedOrderDto, actualOrder);
      assertThat(listAppender.list)
              .extracting(ILoggingEvent::getFormattedMessage, ILoggingEvent::getLevel)
              .containsExactly(Tuple.tuple(String.format("Verify if order %s exists.", id1), Level.INFO));
   }

   @Test
   void shouldNotFindOrderById () {
      //arrange
      when(orderRepository.findOrderById(id1)).thenThrow(domainException);

      //act
      DomainException actualDomainException = assertThrows(DomainException.class, () -> orderServiceImpl.findOrderById(id1));

      //assert
      assertEquals(domainException, actualDomainException);
      assertThat(listAppender.list)
              .extracting(ILoggingEvent::getFormattedMessage, ILoggingEvent::getLevel)
              .containsExactly(Tuple.tuple(String.format("Verify if order %s exists.", id1), Level.INFO));
   }

   @Test
   void shouldCancelOrder () {
      //arrange
      when(orderRepository.findOrderById(id1)).thenReturn(Optional.ofNullable(completedOrder));
      when(orderMapper.mapOrderToDto(cancelledOrder)).thenReturn(cancelledOrderDto);

      //act
      OrderDto actualOrder = orderServiceImpl.cancelOrder(id1);

      //assert
      verify(orderRepository, times(1)).save(completedOrder);
      verify(orderProducer, times(1)).send(actualOrder);
      assertThat(actualOrder)
              .usingRecursiveComparison()
              .ignoringFields("orderId")
              .isEqualTo(cancelledOrderDto);
      assertThat(listAppender.list)
              .extracting(ILoggingEvent::getFormattedMessage, ILoggingEvent::getLevel)
              .containsExactly(Tuple.tuple(String.format("Verify if order %s exists.", id1), Level.INFO),
                      Tuple.tuple(String.format("Set order %s to cancelled.", id1), Level.INFO),
                      Tuple.tuple(String.format("Updated order %s into database.", id1), Level.INFO),
                      Tuple.tuple(String.format("Sent updated order %s to preparation.", id1), Level.INFO));
   }

   @Test
   void shouldNotCancelOrder () {
      //arrange
      when(orderRepository.findOrderById(id1)).thenThrow(domainException);

      //act
      DomainException actualDomainException = assertThrows(DomainException.class, () -> orderServiceImpl.cancelOrder(id1));

      //assert
      assertThat(actualDomainException)
              .usingRecursiveComparison()
              .ignoringFields("requestDateTime")
              .isEqualTo(domainException);
      assertThat(listAppender.list)
              .extracting(ILoggingEvent::getFormattedMessage, ILoggingEvent::getLevel)
              .containsExactly(Tuple.tuple(String.format("Verify if order %s exists.", id1), Level.INFO));
   }
}
