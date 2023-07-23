package hello.springadvanced.app.v4;

import hello.springadvanced.trace.logtrace.LogTrace;
import hello.springadvanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV4 {

  private final LogTrace trace;

  public void save(String itemId) {
    AbstractTemplate<Void> template = new AbstractTemplate<Void>(trace) {
      @Override
      protected Void call() {
        // 저장 로직
        if (itemId.equals("ex")) {
          throw new IllegalStateException("Exception!!!");
        }
        sleep(1000);
        return null;
      }
    };

    template.execute("OrderRepository.save()");
  }

  private void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
