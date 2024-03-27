package sample.cafekiosk.spring.api.controller.order.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;

public record OrderCreateRequest(
    @NotEmpty(message = "상품 번호 리스트는 필수입니다.") List<String> productNumbers
) {

  public OrderCreateServiceRequest toService() {
    return new OrderCreateServiceRequest(this.productNumbers);
  }
}
