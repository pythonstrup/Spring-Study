package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.HOLD;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.STOP_SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.IntegrationTestSupport;

//@ActiveProfiles("test")
//@DataJpaTest
@Transactional
class ProductRepositoryTest  extends IntegrationTestSupport {

  @Autowired
  private ProductRepository productRepository;

  @Test
  @DisplayName("원하는 판매상태를 가진 판매 상품을 조회한다.")
  public void findBySellingTypeIn() {
    // given
    Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
    Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
    Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
    productRepository.saveAll(List.of(product1, product2, product3));

    // when
    List<Product> products = productRepository.findBySellingTypeIn(List.of(SELLING, HOLD));

    // then
    assertThat(products).hasSize(2)
        .extracting("productNumber", "name", "sellingType")
        .containsExactlyInAnyOrder(
            tuple("001", "아메리카노", SELLING),
            tuple("002", "카페라떼", HOLD)
        );
  }

  @Test
  @DisplayName("상품 번호 리스트로 판매 상품을 조회한다.")
  public void findByProductNumberIn() {
    // given
    Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
    Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
    Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
    productRepository.saveAll(List.of(product1, product2, product3));

    // when
    List<Product> products = productRepository.findByProductNumberIn(List.of("001", "002"));

    // then
    assertThat(products).hasSize(2)
        .extracting("productNumber", "name", "sellingType")
        .containsExactlyInAnyOrder(
            tuple("001", "아메리카노", SELLING),
            tuple("002", "카페라떼", HOLD)
        );
  }

  @Test
  @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다.")
  public void findLatestProduct() {
    // given
    String targetProductNumber = "003";

    Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
    Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
    Product product3 = createProduct(targetProductNumber, HANDMADE, STOP_SELLING, "팥빙수", 7000);
    productRepository.saveAll(List.of(product1, product2, product3));

    // when
    String latestProductNumber = productRepository.findLatestProduct();

    // then
    assertThat(latestProductNumber).isEqualTo(targetProductNumber);
  }

  @Test
  @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 때, 상품이 하나도 없는 경우에는 null을 반환한다.")
  public void findLatestProductWhenProductIsEmpty() {
    // given

    // when
    String latestProductNumber = productRepository.findLatestProduct();

    // then
    assertThat(latestProductNumber).isNull();
  }

  private Product createProduct(
      String productNumber,
      ProductType type,
      ProductSellingType sellingType,
      String name,
      int price) {
    return Product.builder()
        .productNumber(productNumber)
        .type(type)
        .sellingType(sellingType)
        .name(name)
        .price(price)
        .build();
  }
}