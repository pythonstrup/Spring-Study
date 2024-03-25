package sample.cafekiosk.spring.api.controller.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

public record ProductCreateRequest(
    @NotNull ProductType type,
    @NotNull ProductSellingType sellingType,
    @NotBlank String name,
    @Positive int price
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
