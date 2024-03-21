package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;

  public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredAt) {
    List<String> productNumbers = request.productNumbers();
    List<Product> products = productRepository.findByProductNumberIn(productNumbers);
    Order order = Order.create(products, registeredAt);
    Order savedOrder = orderRepository.save(order);
    return OrderResponse.of(savedOrder);
  }
}
