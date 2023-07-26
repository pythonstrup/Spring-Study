package hello.springadvanced.app.v5;

import hello.springadvanced.trace.callback.TraceTemplate;
import hello.springadvanced.trace.logtrace.LogTrace;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryV5 {

  private final TraceTemplate template;

  public OrderRepositoryV5(LogTrace logTrace) {
    this.template = new TraceTemplate(logTrace);
  }

  public void save(String itemId) {
    template.execute("OrderRepository.save()", () -> {
      if (itemId.equals("ex")) {
        throw new IllegalStateException("Exception!!!");
      }
      sleep(1000);
      return null;
    });
  }

  private void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
