package sample.cafekiosk.spring.api.controller.order;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.OrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

  private OrderService orderService;

  @PostMapping("/new")
  public void createOrder(@RequestBody OrderCreateRequest request) {
    LocalDateTime registeredAt = LocalDateTime.now();
    orderService.createOrder(request, registeredAt);
  }
}
