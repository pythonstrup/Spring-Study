package sample.cafekiosk.spring.api.service.order.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.orderproduct.OrderProduct;

public record OrderResponse(
    Long id,
    int totalPrice,
    LocalDateTime registeredAt,
    List<ProductResponse> products
) {

  public static OrderResponse of(Order order) {
    return new OrderResponse(
        order.getId(),
        order.getTotalPrice(),
        order.getRegisteredAt(),
        order.getOrderProducts()
            .stream()
            .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
            .collect(Collectors.toList()));
  }
}
