package jpabook.jpashop.service;

import java.util.List;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepositoryOld;
import jpabook.jpashop.repository.order.OrderRepository;
import jpabook.jpashop.repository.order.OrderSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

  private final OrderRepository orderRepository;
  private final MemberRepositoryOld memberRepository;
  private final ItemRepository itemRepository;

  /**
   * 주문
   */
  @Transactional
  public Long order(Long memberId, Long itemId, int count) {
    // Entity 조회
    Member member = memberRepository.find(memberId);
    Item item = itemRepository.find(itemId);

    // 배송정보 생성
    Delivery delivery = new Delivery();
    delivery.setAddress(member.getAddress());

    // 주문 상품 생성
    OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

    // 주문 생성
    Order order = Order.createOrder(member, delivery, orderItem);

    // 주문 저장
    Long savedId = orderRepository.save(order);

//    return savedId;
    return order.getId(); // 영속성!!
  }

  /**
   * 주문 취소
   */
  @Transactional
  public void cancelOrder(Long orderId) {
    // 엔티티 조회
    Order order = orderRepository.find(orderId);

    // 주문 취소
    order.cancel();
  }

  /**
   * 검색
   */
  public List<Order> findOrders(OrderSearch orderSearch) {
//    return orderRepository.findAllByCriteria(orderSearch);
    log.info("QueryDSL!!!");
    return orderRepository.findAll(orderSearch);
  }
}
