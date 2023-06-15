package jpabook.jpashop.dto.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;

@Getter
public class OrderDto {

  private Long orderId;
  private String name;
  private LocalDateTime orderDate;
  private OrderStatus orderStatus;
  private Address address;
  private List<OrderItemDto> orderItems;

  public OrderDto(Order order) {
    orderId = order.getId();
    name = order.getMember().getUsername();
    orderDate = order.getOrderDate();
    orderStatus = order.getStatus();
    address = order.getDelivery().getAddress();

//      order.getOrderItems().stream().forEach(o -> o.getItem().getName()); // LAZY 초기화
//      orderItems = order.getOrderItems();

    orderItems = order.getOrderItems().stream()
        .map(OrderItemDto::new)
        .collect(Collectors.toList());
  }
}
