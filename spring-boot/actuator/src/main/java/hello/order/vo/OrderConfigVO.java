package hello.order.vo;

import hello.order.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfigVO {

  @Bean
  OrderService orderService() {
    return new OrderServiceVO();
  }
}
