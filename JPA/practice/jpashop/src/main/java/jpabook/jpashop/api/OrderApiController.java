package jpabook.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

  private final OrderRepository orderRepository;

  // 바람직하지 않은 방법
  @GetMapping("/api/v1/orders")
  public List<Order> ordersV1() {
    List<Order> all = orderRepository.findAllByString(new OrderSearch());
    for (Order order : all) {
      order.getMember().getUsername(); // LAZY 로딩 엔티티 강제 초기화
      order.getDelivery().getAddress();
      List<OrderItem> orderItems = order.getOrderItems();
      orderItems.stream().forEach(o -> o.getItem().getName()); // orderItems도 초기화
    }
    return all;
  }

  // 쿼리가 엄청 많이 나간다. => Collection을 사용하면 LAZY를 사용할 때 쿼리가 많이 나갈 수밖에 없다.
  @GetMapping("/api/v2/orders")
  public List<OrderDto> ordersV2() {
    List<Order> orders = orderRepository.findAllByString(new OrderSearch());
    List<OrderDto> result = orders.stream()
        .map(OrderDto::new)
        .collect(Collectors.toList());
    return result;
  }

  @Getter
  static class OrderDto {

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

  @Getter
  static class OrderItemDto {

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
}
