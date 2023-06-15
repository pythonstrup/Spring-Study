package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jpabook.jpashop.dto.OrderItemQueryDto;
import jpabook.jpashop.dto.OrderQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

  private final EntityManager em;

  // 결과적으로 N+1 문제 발생... orderItem의 개수만큼 조회한다..
  public List<OrderQueryDto> findOrderQueryDtos() {
    List<OrderQueryDto> result = findOrders();
    result.forEach(o -> {
      List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
      o.setOrderItems(orderItems);
    });
    return result;
  }

  private List<OrderItemQueryDto> findOrderItems(Long orderId) {
    return em.createQuery(
        "select new jpabook.jpashop.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) "
            + "from OrderItem oi "
            + "join oi.item i "
            + "where oi.order.id = :orderId", OrderItemQueryDto.class)
        .setParameter("orderId", orderId)
        .getResultList();
  }

  private List<OrderQueryDto> findOrders() {
    return em.createQuery(
        "select new jpabook.jpashop.dto.OrderQueryDto(o.id, m.username, o.orderDate, o.status, d.address) "
            + "from Order o "
            + "join o.member m "
            + "join o.delivery d", OrderQueryDto.class
    ).getResultList();
  }

  public List<OrderQueryDto> findOrderQueryDtosOptimization() {
    List<OrderQueryDto> result = findOrders();

    Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(getOrderIds(result));

    result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

    return result;
  }

  private static List<Long> getOrderIds(List<OrderQueryDto> result) {
    return result.stream()
        .map(o -> o.getOrderId())
        .collect(Collectors.toList());
  }

  private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
    List<OrderItemQueryDto> orderItems = em.createQuery(
            "select new jpabook.jpashop.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) "
                + "from OrderItem oi "
                + "join oi.item i "
                + "where oi.order.id in :orderIds", OrderItemQueryDto.class)
        .setParameter("orderIds", orderIds)
        .getResultList();
    Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
        .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
    return orderItemMap;
  }

}
