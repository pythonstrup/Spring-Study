package sample.cafekiosk.spring.api.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;

class OrderStatisticsServiceTest extends IntegrationTestSupport {

  @Autowired
  private OrderStatisticsService orderStatisticsService;

  @Autowired
  OrderProductRepository orderProductRepository;

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  MailSendHistoryRepository mailSendHistoryRepository;

  @AfterEach
  void tearDown() {
    orderProductRepository.deleteAllInBatch();
    orderRepository.deleteAllInBatch();
    productRepository.deleteAllInBatch();
    mailSendHistoryRepository.deleteAllInBatch();
  }

  @Test
  @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
  public void sendOrderStatisticsMail() {
    // given
    LocalDateTime now = LocalDateTime.of(2024, 3, 5, 0, 0);

    Product product1 = createProduct(HANDMADE, "001", 1000);
    Product product2 = createProduct(HANDMADE, "002", 3000);
    Product product3 = createProduct(HANDMADE, "003", 5000);
    List<Product> products = List.of(product1, product2, product3);
    productRepository.saveAll(products);

    Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2024, 3, 4, 23, 59, 59), products);
    Order order2 = createPaymentCompletedOrder(now, products);
    Order order3 = createPaymentCompletedOrder(LocalDateTime.of(2024, 3, 5, 23, 59, 59), products);
    Order order4 = createPaymentCompletedOrder(LocalDateTime.of(2024, 3, 6, 0, 0), products);

    // stubbing
    Mockito.when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
        .thenReturn(true);

    // when
    boolean result =
        orderStatisticsService.sendOrderStatisticsMail(
            LocalDate.of(2024, 3, 5), "test@test.com");

    // then
    assertThat(result).isTrue();

    List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
    assertThat(histories).hasSize(1)
        .extracting("content")
        .contains("총 매출 합계는 18000원입니다.");
  }

  private Order createPaymentCompletedOrder(LocalDateTime now, List<Product> products) {
    Order order = Order.builder()
        .products(products)
        .orderStatus(OrderStatus.PAYMENT_COMPLETE)
        .registeredAt(now)
        .build();
    return orderRepository.save(order);
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