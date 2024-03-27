package sample.cafekiosk.spring.api.service.product.request;

import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

public record ProductCreateServiceRequest(
    ProductType type,
    ProductSellingType sellingType,
    String name,
    int price
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
