package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV2 {

  /**
   * ApplicationContext는 너무 거대하다. (수많은 기능이 있음)
   * 차라리 다른 것을 사용하는 것이 좋아보인다.
   */
//  private final ApplicationContext applicationContext;
//  public CallServiceV2(ApplicationContext applicationContext) {
//    this.applicationContext = applicationContext;
//  }

  private final ObjectProvider<CallServiceV2> callServiceProvider;

  public CallServiceV2(ObjectProvider<CallServiceV2> callServiceProvider) {
    this.callServiceProvider = callServiceProvider;
  }

  // 지연해서 Bean 꺼내기
  public void external() {
    log.info("call external");
//    CallServiceV2 callServiceV2 = applicationContext.getBean(CallServiceV2.class);
    CallServiceV2 callServiceV2 = callServiceProvider.getObject();
    callServiceV2.internal(); // 외부 메소드 호출
  }

  public void internal() {
    log.info("call internal");
  }
}
