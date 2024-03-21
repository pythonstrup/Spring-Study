package sample.cafekiosk.spring.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.spring.domain.product.Product;

class OrderTest {

  @Test
  @DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산하고 주문 상태는 INIT, 등록시간이 입력된다.")
  public void create() {
    // given
    List<Product> products = List.of(createProduct("001", 1000), createProduct("002", 2000));

    // when
    Order order = Order.create(products, LocalDateTime.of(2024, 3, 3, 3, 3));

    // then
    assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.INIT);
    assertThat(order.getTotalPrice()).isEqualTo(3000);
    assertThat(order.getRegisteredAt()).isEqualTo(LocalDateTime.of(2024, 3, 3, 3, 3));
  }

  private Product createProduct(String productNumber, int price) {
    return Product.builder()
        .type(HANDMADE)
        .productNumber(productNumber)
        .price(price)
        .sellingType(SELLING)
        .name("메뉴 이름")
        .build();
  }
}