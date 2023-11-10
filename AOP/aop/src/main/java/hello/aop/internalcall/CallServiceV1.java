package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV1 {

  private CallServiceV1 callServiceV1;

  /**
   * 참고: 생성자 주입은 순환 사이클을 만들기 때문에 실패한다.
   * => 자기자신이 생성도 안됐는데 어떻게 주입이 되겠는가?
   */
//  @Autowired
//  public CallServiceV1(CallServiceV1 callServiceV1) {
//    this.callServiceV1 = callServiceV1;
//  }

  /**
   * 대신 setter를 사용하면 된다. => 그런데 사실 스프링부트 2.6부터 이 방식도 막혔다.
   * 원칙적으로 순환참조를 금지하도록 변경된 것이다.
   * application.properties에 `spring.main.allow-circular-references=true` 설정을 추가해야한다.
   */
  @Autowired
  public void setCallServiceV1(CallServiceV1 callServiceV1) {
    log.info("callServiceV1 setter={}", callServiceV1.getClass());
    this.callServiceV1 = callServiceV1;
  }

  public void external() {
    log.info("call external");
    callServiceV1.internal(); // 외부 메소드 호출
  }

  public void internal() {
    log.info("call internal");
  }
}
