package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.HOLD;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.STOP_SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
//@SpringBootTest
@DataJpaTest
class ProductRepositoryTest {

  @Autowired
  private ProductRepository productRepository;

  @Test
  @DisplayName("원하는 판매상태를 가진 판매 상품을 조회한다.")
  public void findBySellingTypeIn() {
    // given
    Product product1 = Product.builder()
        .productNumber("001")
        .type(HANDMADE)
        .sellingType(SELLING)
        .name("아메리카노")
        .price(4000)
        .build();
    Product product2 = Product.builder()
        .productNumber("002")
        .type(HANDMADE)
        .sellingType(HOLD)
        .name("카페라떼")
        .price(4500)
        .build();
    Product product3 = Product.builder()
        .productNumber("003")
        .type(HANDMADE)
        .sellingType(STOP_SELLING)
        .name("팥빙수")
        .price(7000)
        .build();
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
    Product product1 = Product.builder()
        .productNumber("001")
        .type(HANDMADE)
        .sellingType(SELLING)
        .name("아메리카노")
        .price(4000)
        .build();
    Product product2 = Product.builder()
        .productNumber("002")
        .type(HANDMADE)
        .sellingType(HOLD)
        .name("카페라떼")
        .price(4500)
        .build();
    Product product3 = Product.builder()
        .productNumber("003")
        .type(HANDMADE)
        .sellingType(STOP_SELLING)
        .name("팥빙수")
        .price(7000)
        .build();
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
}