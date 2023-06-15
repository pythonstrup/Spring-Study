package jpabook.jpashop.service.query;

import java.util.List;
import java.util.stream.Collectors;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.dto.order.OrderDto;
import jpabook.jpashop.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {

  private final OrderRepository orderRepository;

  // Open Session In View 설정
  // OSIV가 OFF라면 아래와 같이 LAZY 로딩이 있는 로직을 Transaction 안에 넣어줘야 한다.
  public List<OrderDto> ordersV3() {
    List<Order> orders = orderRepository.findAllWithItem();
    return orders.stream()
        .map(OrderDto::new)
        .collect(Collectors.toList());
  }
}

// 보통 비즈니스 로직은 엔티티를 등록하거나 수정하는 것이므로 성능에 크게 문제가 되지 않지만
// 복잡한 화면을 출력하기 위한 쿼리는 화면에 맞추어 성능을 최적화하는 것이 중요하다. 하지만 그 복잡성에 비해 핵심 비즈니스에 큰 영향을 주지는 않는다.
// 크고 복잡한 애플리케이션을 개발한다면, 이 둘의 관심사를 명확하게 분리하는 선택이 유지보수 관점에서 충분히 의미가 있다.
// 아래와 같이 분리할 수 있을 것이다.
// OrderService(1.OrderService-핵심로직 / 2.OrderQueryService-화면, API 맞춤 서비스(주로 읽기 전용 트랜잭션))

// 실시간 API는 OSIV를 끄고, ADMIN처럼 커넥션을 많이 사용하지 않는 곳에서는 OSIV를 켜는 것이 좋다.
// 성능이슈가 있는 애플리케이션에서 OSIV가 켜져있으면 커넥션이 말라버릴 수 있다는 것을 명심하자.
