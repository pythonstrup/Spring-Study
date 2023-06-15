package jpabook.jpashop.dto.order;

import jpabook.jpashop.domain.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemDto {
  // 필요한 정보만 뽑아올 수 있다.
  private String itemName;
  private int price;
  private int count;

  public OrderItemDto(OrderItem orderItem) {
    itemName = orderItem.getItem().getName();
    price = orderItem.getOrderPrice();
    count = orderItem.getCount();
  }
}
