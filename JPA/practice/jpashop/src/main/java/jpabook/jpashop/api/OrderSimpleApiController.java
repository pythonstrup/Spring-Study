package jpabook.jpashop.api;

import java.util.List;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
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


}
