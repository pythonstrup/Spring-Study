package sample.cafekiosk.spring.api.service.product.response;

import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

public record ProductResponse(
    Long id,
    String productNumber,
    ProductType type,
    ProductSellingType sellingType,
    String name,
    int price
) {

  public static ProductResponse of(Product product) {
    return new ProductResponse(
        product.getId(),
        product.getProductNumber(),
        product.getType(),
        product.getSellingType(),
        product.getName(),
        product.getPrice()
    );
  }
}
