package jpabook.jpashop.service;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.order.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

  @Autowired
  EntityManager em;
  @Autowired
  OrderService orderService;
  @Autowired
  OrderRepository orderRepository;

  @Test
  public void order() throws Exception {
    // given
    Member member = createMember();

    Book book = createBook("JPA", 10000, 10);

    // when
    int orderCount = 2;
    Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

    // then
    Order getOrder = orderRepository.find(orderId);
    assertEquals(OrderStatus.ORDER, getOrder.getStatus());
    assertEquals(1, getOrder.getOrderItems().size());
    assertEquals(10000 * orderCount, getOrder.getTotalPrice());
    assertEquals(8, book.getStockQuantity());
  }

  // 주문 취소
  @Test
  public void cancel() throws Exception {
    // given
    Member member = createMember();
    Book book = createBook("JPA", 10000, 10);
    int orderCount = 2;
    Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

    // when
    orderService.cancelOrder(orderId);

    // then
    Order getOrder = orderRepository.find(orderId);
    assertEquals(OrderStatus.CANCEL, getOrder.getStatus());
    assertEquals(10, book.getStockQuantity());
  }

  // 재고 수량 초과
  @Test
  public void overStock() throws Exception {
    // given
    Member member = createMember();
    Book book = createBook("JPA", 10000, 10);
    int orderCount = 11;

    // when
    // then
    assertThrows(NotEnoughStockException.class, () -> {
      orderService.order(member.getId(), book.getId(), orderCount);
    });
  }


  // 기본 세팅
  private Book createBook(String name, int price, int stockQuantity) {
    Book book = new Book();
    book.setName(name);
    book.setPrice(price);
    book.setStockQuantity(stockQuantity);
    em.persist(book);
    return book;
  }

  private Member createMember() {
    Member member = new Member();
    member.setUsername("park");
    member.setAddress(new Address("seoul", "ro", "11111"));
    em.persist(member);
    return member;
  }
}