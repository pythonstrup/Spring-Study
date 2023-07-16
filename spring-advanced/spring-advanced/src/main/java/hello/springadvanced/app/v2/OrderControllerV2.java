package hello.springadvanced.app.v2;

import hello.springadvanced.trace.TraceStatus;
import hello.springadvanced.trace.helloTrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV2 {

  private final HelloTraceV2 trace;
  private final OrderServiceV2 orderService;

  @GetMapping("/v2/request")
  public String request(String itemId) {

    TraceStatus status = null;
    try {
      status = trace.begin("OrderController.request()");
      orderService.orderItem(status.getTraceId(), itemId);
      trace.end(status);
      return "ok";
    } catch (Exception e) {
      trace.exception(status, e);
      throw e;
    }
  }
}
