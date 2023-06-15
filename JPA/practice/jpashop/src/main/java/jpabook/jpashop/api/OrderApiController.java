package jpabook.jpashop.api;

import java.util.List;
import java.util.stream.Collectors;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.dto.order.OrderDto;
import jpabook.jpashop.dto.order.OrderFlatDto;
import jpabook.jpashop.dto.order.OrderItemQueryDto;
import jpabook.jpashop.dto.order.OrderQueryDto;
import jpabook.jpashop.repository.order.OrderRepository;
import jpabook.jpashop.repository.order.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

  private final OrderRepository orderRepository;
  private final OrderQueryRepository orderQueryRepository;

  // 바람직하지 않은 방법
  // 그리고 OSIV가 OFF되면 오류가 터질 수 있다. => LAZY 로딩을 트랜잭션(서비스)으로 끌고 들어가야 한다...
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

  // batchSize로 쿼리 호출 수가 1+N에서 1+1로 최적화된다.
  // 데이터베이스에 따라 IN절 파라미터 개수를 1000개로 제한하기도 한다.
  // 1000으로 설정하는 것이 성능상 가장 좋지만, DB나 애플리케이션이 순간 부하를 어디까지 견딜 수 있는지로 결정하면 된다.
  @GetMapping("/api/v3.1/orders")
  public List<OrderDto> ordersV3_page(
      @RequestParam(value = "offset",defaultValue = "0") int offset,
      @RequestParam(value = "limit",defaultValue = "100") int limit
      ) {
    List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
    return orders.stream()
        .map(OrderDto::new)
        .collect(Collectors.toList());
  }

  // N+1 문제 발생함
  @GetMapping("/api/v4/orders")
  public List<OrderQueryDto> ordersV4() {
    return orderQueryRepository.findOrderQueryDtos();
  }

  // O(1)로 최적화 => 예제에서는 쿼리 2번 나감
  @GetMapping("/api/v5/orders")
  public List<OrderQueryDto> ordersV5() {
    return orderQueryRepository.findOrderQueryDtosOptimization();
  }

  // 쿼리가 한 번 나간다는 장점이 있지만...
  // 데이터에 중복데이터가 추가된다는 점에서 V5보다 느릴 수 있고, 애플리케이션에서의 추가 작업이 많아진다.
  // 그리고 페이징이 불가능하다.
  @GetMapping("/api/v6/orders")
  public List<OrderQueryDto> ordersV6() {
    List<OrderFlatDto> flats = orderQueryRepository.findOrderQueryDtosFlat();
    return flats.stream()
        .collect(Collectors.groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(),o.getAddress()),
            Collectors.mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), Collectors.toList())
            )).entrySet().stream()
        .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(),
            e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
        .collect(Collectors.toList());
  }
}
