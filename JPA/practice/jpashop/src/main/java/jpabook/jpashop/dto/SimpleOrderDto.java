package jpabook.jpashop.dto;

import java.time.LocalDateTime;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

@Data
public class SimpleOrderDto {
  private Long orderId;
  private String name;
  private LocalDateTime orderDate;
  private OrderStatus orderStatus;
  private Address address;

  // V2~V3
  public SimpleOrderDto(Order order) {
    orderId = order.getId();
    name = order.getMember().getUsername(); // LAZY 초기화
    orderDate = order.getOrderDate();
    orderStatus = order.getStatus();
    address = order.getDelivery().getAddress(); // LAZY 초기화
  }

  // V4
  public SimpleOrderDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
    this.orderId = orderId;
    this.name = name;
    this.orderDate = orderDate;
    this.orderStatus = orderStatus;
    this.address = address;
  }
}