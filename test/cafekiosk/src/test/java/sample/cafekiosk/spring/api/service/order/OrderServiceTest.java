package sample.cafekiosk.spring.api.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

  @Autowired
  OrderService orderService;

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  OrderProductRepository orderProductRepository;

  @Autowired
  ProductRepository productRepository;

  @AfterEach
  void tearDown() {
    orderProductRepository.deleteAllInBatch();
    productRepository.deleteAllInBatch();
    orderRepository.deleteAllInBatch();
  }

  @Test
  @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
  public void createOrder() {
    // given
    Product product1 = createProduct(HANDMADE, "001", 1000);
    Product product2 = createProduct(HANDMADE, "002", 3000);
    Product product3 = createProduct(HANDMADE, "003", 5000);
    productRepository.saveAll(List.of(product1, product2, product3));
    OrderCreateRequest request = new OrderCreateRequest(List.of("001", "002"));
    LocalDateTime registeredAt = LocalDateTime.of(2024, 3, 3, 3, 3);

    // when
    OrderResponse result = orderService.createOrder(request, registeredAt);

    // then
    assertThat(result.id()).isNotNull();
    assertThat(result)
        .extracting("registeredAt", "totalPrice")
        .contains(LocalDateTime.of(2024, 3, 3, 3, 3), 4000);
    assertThat(result.products()).hasSize(2)
        .extracting("productNumber", "price")
        .containsExactlyInAnyOrder(
            tuple("001", 1000),
            tuple("002", 3000)
        );
  }

  @Test
  @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다")
  public void createOrderWithDulplicateProductNumber() {
    // given
    Product product1 = createProduct(HANDMADE, "001", 1000);
    Product product2 = createProduct(HANDMADE, "002", 3000);
    Product product3 = createProduct(HANDMADE, "003", 5000);
    productRepository.saveAll(List.of(product1, product2, product3));
    OrderCreateRequest request = new OrderCreateRequest(List.of("001", "001"));
    LocalDateTime registeredAt = LocalDateTime.of(2024, 3, 3, 3, 3);

    // when
    OrderResponse result = orderService.createOrder(request, registeredAt);

    // then
    assertThat(result.id()).isNotNull();
    assertThat(result)
        .extracting("registeredAt", "totalPrice")
        .contains(LocalDateTime.of(2024, 3, 3, 3, 3), 2000);
    assertThat(result.products()).hasSize(2)
        .extracting("productNumber", "price")
        .containsExactlyInAnyOrder(
            tuple("001", 1000),
            tuple("001", 1000)
        );
  }

  private Product createProduct(ProductType type, String productNumber, int price) {
    return Product.builder()
        .type(type)
        .productNumber(productNumber)
        .price(price)
        .sellingType(SELLING)
        .name("메뉴 이름")
        .build();
  }
}