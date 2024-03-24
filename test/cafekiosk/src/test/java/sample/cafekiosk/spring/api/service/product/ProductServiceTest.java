package sample.cafekiosk.spring.api.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

@SpringBootTest
//@Transactional
@ActiveProfiles("test")
class ProductServiceTest {

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductRepository productRepository;

  @AfterEach
  void tearDown() {
    productRepository.deleteAllInBatch();
  }

  @Test
  @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
  public void createProduct() {
    // given
    Product product1 = createProduct(
        "001", HANDMADE, SELLING, "아메리카노", 4000);
    productRepository.saveAll(List.of(product1));

    ProductCreateRequest request = new ProductCreateRequest(HANDMADE, SELLING, "카푸치노", 5000);

    // when
    ProductResponse productResponse = productService.createProduct(request);

    // then
    assertThat(productResponse)
        .extracting("productNumber", "type", "sellingType", "name", "price")
        .contains("002", HANDMADE, SELLING, "카푸치노", 5000);

    List<Product> products = productRepository.findAll();
    assertThat(products).hasSize(2)
        .extracting("productNumber", "type", "sellingType", "name", "price")
        .containsExactlyInAnyOrder(
            tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
            tuple("002", HANDMADE, SELLING, "카푸치노", 5000));
  }

  @Test
  @DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 가장 001이다.")
  public void createProductWhenProductIsEmpty() {
    // given
    ProductCreateRequest request = new ProductCreateRequest(HANDMADE, SELLING, "카푸치노", 5000);

    // when
    ProductResponse productResponse = productService.createProduct(request);

    // then
    assertThat(productResponse)
        .extracting("productNumber", "type", "sellingType", "name", "price")
        .contains("001", HANDMADE, SELLING, "카푸치노", 5000);

    List<Product> products = productRepository.findAll();
    assertThat(products).hasSize(1)
        .extracting("productNumber", "type", "sellingType", "name", "price")
        .containsExactlyInAnyOrder(
            tuple("001", HANDMADE, SELLING, "카푸치노", 5000));
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