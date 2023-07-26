package hello.springadvanced.app.v5;

import hello.springadvanced.trace.callback.TraceCallback;
import hello.springadvanced.trace.callback.TraceTemplate;
import hello.springadvanced.trace.logtrace.LogTrace;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderControllerV5 {

  private final OrderServiceV5 orderService;
  private final TraceTemplate template;

  public OrderControllerV5(OrderServiceV5 orderService, LogTrace logTrace) {
    this.orderService = orderService;
    this.template = new TraceTemplate(logTrace);
  }

  @GetMapping("/v5/request")
  public String request(String itemId) {
    return template.execute("OrderController.request()", new TraceCallback<>() {
      @Override
      public String call() {
        orderService.orderItem(itemId);
        return "ok";
      }
    });
  }
}
