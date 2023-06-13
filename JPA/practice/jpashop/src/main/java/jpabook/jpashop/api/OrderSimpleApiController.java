package jpabook.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * X TO ONE (ManyToOne, OneToOne)에서의 성능 최적화
 * Order 조회
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    // 좋지 않는 방식 - 엔티티를 그대로 노출
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
      List<Order> all = orderRepository.findAllByString(new OrderSearch());
      for (Order order : all) {
        order.getMember().getUsername(); // Lazy 강제 초기화
        order.getDelivery().getAddress(); // Lazy 강제 초기화
      }
      return all;
    }

    // START: ORDER -> SQL 1번 (결과 2건)
    // LOOP1: MEMBER, DELIVERY
    // LOOP2: MEMBER, DELIVERY
    // 총 5번의 쿼리가 나간다. (1 + N + N 번 쿼리가 나간다.)
    // EAGER로 해도 동일한 문제가 발생한다.
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
      List<Order> orders = orderRepository.findAllByString(new OrderSearch());
      List<SimpleOrderDto> result = orders.stream()
          .map(SimpleOrderDto::new)
          .collect(Collectors.toList());

      return result;
    }

    // 한방 쿼리 (N+1 문제 해결)
  @GetMapping("/api/v3/simple-orders")
  public List<SimpleOrderDto> ordersV3() {
    List<Order> orders = orderRepository.findAllWithMemberDelivery();
    List<SimpleOrderDto> result = orders.stream()
        .map(SimpleOrderDto::new)
        .collect(Collectors.toList());
    return result;
  }

    @Data
    static class SimpleOrderDto {
      private Long orderId;
      private String name;
      private LocalDateTime orderDate;
      private OrderStatus orderStatus;
      private Address address;

      public SimpleOrderDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getUsername(); // LAZY 초기화
        orderDate = order.getOrderDate();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress(); // LAZY 초기화
      }
    }
}
