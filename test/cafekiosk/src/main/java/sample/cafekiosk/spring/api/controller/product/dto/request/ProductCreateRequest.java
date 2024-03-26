package sample.cafekiosk.spring.api.controller.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

public record ProductCreateRequest(
    @NotNull(message = "상품 타입은 필수입니다.") ProductType type,
    @NotNull(message = "상품 판매상태는 필수입니다.") ProductSellingType sellingType,
    @NotBlank(message = "상품 이름은 필수입니다.") String name,
    @Positive(message = "상품 가격은 양수여야 합니다.") int price
) {

  public Product toEntity(String nextProductNumber) {
    return Product.builder()
        .productNumber(nextProductNumber)
        .type(type)
        .sellingType(sellingType)
        .name(name)
        .price(price)
        .build();
  }
}
