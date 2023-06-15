package jpabook.jpashop.api;

import java.util.List;
import java.util.stream.Collectors;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.dto.order.SimpleOrderDto;
import jpabook.jpashop.repository.order.OrderRepository;
import jpabook.jpashop.repository.order.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
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

  // OrderRepository 단순히 order 엔티티를 조회할 때 사용한다.
    private final OrderRepository orderRepository;

    // 반면 OrderSimpleQueryRepository는 DTO를 뽑아내거나, 복잡한 쿼리를 만들 때 사용한다.
    // 만약 쿼리가 일반 Repository에 있으면 용도가 약간 애매해질 수 있다.
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

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

    // 재사용성이 약간 떨어질 수 있다는 단점이 존재한다.
    @GetMapping("/api/v4/simple-orders")
    public List<SimpleOrderDto> ordersV4() {
//        return orderRepository.findOrderDtos();
      return orderSimpleQueryRepository.findOrderDtos();
    }

  // 권장하는 방법 (4단계)
  // 1. 우선 엔티티를 DTO로 변환하는 방법을 선택한다.
  // 2. 필요하면 페치 조인으로 성능을 최적화한다. => 대부분의 성능 이슈 해결된다.
  // 3. 그래도 안되면 DTO로 직접 조회하는 방법을 사용한다.
  // 4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template를 사용해서 SQL을 직접 사용한다.
}
