package jpabook.jpashop.repository.order.simplequery;

import jakarta.persistence.EntityManager;
import java.util.List;
import jpabook.jpashop.dto.order.SimpleOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

  private final EntityManager em;

  public List<SimpleOrderDto> findOrderDtos() {
    // 엔티티를 그대로 넘기면 식별자가 넘어오기 때문에 파라미터를 한땀한땀 넣어줘야 한다.
    return em.createQuery(
        "select new jpabook.jpashop.dto.order.SimpleOrderDto(o.id, m.username, o.orderDate, o.status, d.address) "
            + "from Order o "
            + "join o.member m "
            + "join o.delivery d", SimpleOrderDto.class).getResultList();
  }
}
