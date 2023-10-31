package hello.aop.pointcut;

import hello.aop.order.aop.member.MemberService;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

@Slf4j
public class ExecutionTest {

  AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
  Method helloMethod;

  @BeforeEach
  public void init() throws NoSuchMethodException {
    helloMethod = MemberService.class.getMethod("hello", String.class);
  }

  @Test
  void printMethod() {
    log.info("helloMethod={}", helloMethod);
  }
}
