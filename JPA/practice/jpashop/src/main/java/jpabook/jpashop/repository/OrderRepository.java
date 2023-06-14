package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.dto.SimpleOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

  private final EntityManager em;

  public Long save(Order order) {
    em.persist(order);
    return order.getId();
  }

  public Order find(Long id) {
    return em.find(Order.class, id);
  }

  // 정적인 방법
  public List<Order> findAllByStatic(OrderSearch orderSearch) {
    String jpql = "select o from Order o join o.member m "
        + "where o.status = :status "
        + "and m.username like :username";
    return em.createQuery(jpql, Order.class)
        .setParameter("status", orderSearch.getOrderStatus())
        .setParameter("username", orderSearch.getMemberName())
        .setMaxResults(100) // 최대 100 건
        .getResultList();
  }

  // 동적 쿼리1
  // 너무 복잡하다.. 권장하는 방법 아님..
  public List<Order> findAllByString(OrderSearch orderSearch) {

    String jpql = "select o from Order o join o.member m";
    boolean isFirstCondition = true;

    //주문 상태 검색
    if (orderSearch.getOrderStatus() != null) {
      if (isFirstCondition) {
        jpql += " where";
        isFirstCondition = false;
      } else {
        jpql += " and";
      }
      jpql += " o.status = :status";
    }

    //회원 이름 검색
    if (StringUtils.hasText(orderSearch.getMemberName())) {
      if (isFirstCondition) {
        jpql += " where";
        isFirstCondition = false;
      } else {
        jpql += " and";
      }
      jpql += " m.username like :username";
    }

    TypedQuery<Order> query = em.createQuery(jpql, Order.class)
        .setMaxResults(1000);

    if (orderSearch.getOrderStatus() != null) {
      query = query.setParameter("status", orderSearch.getOrderStatus());
    }
    if (StringUtils.hasText(orderSearch.getMemberName())) {
      query = query.setParameter("username", orderSearch.getMemberName());
    }

    return query.getResultList();
  }

  // 동적 쿼리2
  // 동적 쿼리를 사용할 때, JPA에서 제공하는 표준 Spec - JPA Criteria
  // 하지만 이 방법도 별로 권장하진 않는다.
  // 무슨 쿼리가 만들어지는 건지 감이 전혀 오지를 않는다. => 유지보수가 너무 어렵다.
  public List<Order> findAllByCriteria(OrderSearch orderSearch) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Order> cq = cb.createQuery(Order.class);
    Root<Order> o = cq.from(Order.class);
    Join<Object, Object> m = o.join("member", JoinType.INNER);

    List<Predicate> criteria = new ArrayList<>();

    //주문 상태 검색
    if (orderSearch.getOrderStatus() != null) {
      Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
      criteria.add(status);
    }
    //회원 이름 검색
    if (StringUtils.hasText(orderSearch.getMemberName())) {
      Predicate name =
          cb.like(m.<String>get("username"), "%" + orderSearch.getMemberName() + "%");
      criteria.add(name);
    }

    cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
    TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
    return query.getResultList();
  }
  // 동적 쿼리 문제를 근본적으로 해결하는 방법은 QueryDSL이다.
  // 나중에 알아볼 것!

  public List<Order> findAllWithMemberDelivery() {
    return em.createQuery(
        "select o from Order o "
            + "join fetch o.member m "
            + "join fetch o.delivery ", Order.class
    ).getResultList();
  }

  public List<SimpleOrderDto> findOrderDtos() {
    // 엔티티를 그대로 넘기면 식별자가 넘어오기 때문에 파라미터를 한땀한땀 넣어줘야 한다.
    return em.createQuery(
        "select new jpabook.jpashop.dto.SimpleOrderDto(o.id, m.username, o.orderDate, o.status, d.address) "
            + "from Order o "
            + "join o.member m "
            + "join o.delivery d", SimpleOrderDto.class).getResultList();
  }

  // Hibernate6 부터는 distinct를 넣어주지 않아도 자동으로 distinct를 넣어준다고 한다.
  // H2 Console에서 join을 직접 실행하면 같은 order_id 하나당 여러 개의 orderItem이 붙기 때문에 중복해서 열이 여러 개 생기는 현상을 확인할 수 있다.
  // DB 쿼리에서는 DISTINCT를 넣어봤자 소용이 없다. 모든 열이 동일해야하기 때문이다.
  // JPA는 distinct를 걸었을 때. order가 같은 값이면 묶어주는 기능이 따로 있는 것이다!!!
  public List<Order> findAllWithItem() {
    return em.createQuery(
        "select distinct o from Order o "
            + "join fetch o.member m "
            + "join fetch o.delivery d "
            + "join fetch o.orderItems oi "
            + "join fetch oi.item i ", Order.class)
//        .setFirstResult(1)
//        .setMaxResults(100)
        .getResultList();
  }
}
