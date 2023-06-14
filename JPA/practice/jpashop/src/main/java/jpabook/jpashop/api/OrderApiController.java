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

  // 한방 쿼리 -> 사실상 V2랑 코드는 똑같다.
  // 하지만 일대다를 fetch join하는 순간 페이징이 불가능해진다. 아래와 같은 경고가 뜬다.
  //  WARN 6367 --- [nio-8080-exec-2] org.hibernate.orm.query: HHH90003004:
  //  firstResult/maxResults specified with collection fetch; applying in memory
  // 실제 쿼리에서는 OFFSET과 LIMIT이 사용되지 않는다.
  // 대신 메모리에서 sorting이 일어나 페이징 처리를 해주는 것이다. 데이터가 많으면 아마도 out of memory 장애가 일어날 수 있다.. 정말 끝장..
  // 일대다 fetch join에서는 페이징을 사용해서는 안된다!!!
  // 또 컬렉션 페치 조인은 1개만 사용할 수 있다. 둘 이상의 컬렉션에 페치 조인을 하면 데이터가 부정합하게 조회될 수 있음을 명심하자.
  @GetMapping("/api/v3/orders")
  public List<OrderDto> ordersV3() {
    List<Order> orders = orderRepository.findAllWithItem();
    return orders.stream()
        .map(OrderDto::new)
        .collect(Collectors.toList());
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
