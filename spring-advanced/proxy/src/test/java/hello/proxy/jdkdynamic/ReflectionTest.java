package hello.proxy.jdkdynamic;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 리플렉션은 가급적 사용하지 않는 것이 좋다.
 * - 런타임에 동작하기 때문에 컴파일 타임에 오류를 잡을 수 없다.
 */
@Slf4j
public class ReflectionTest {

  @Test
  void reflection0() {
    Hello target = new Hello();

    // 공통 로직1 시작
    log.info("start");
    String resutl1 = target.callA();
    log.info("result={}", resutl1);
    // 공통 로직2 종료

    // 공통 로직2 시작
    log.info("start");
    String resutl2 = target.callB();
    log.info("result={}", resutl2);
    // 공통 로직2 종료
  }

  @Test
  void reflection1() throws Exception {
    // 클래스 정보
    Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");
    Hello target = new Hello();

    // callA 메소드 정보
    Method methodCallA = classHello.getMethod("callA");
    Object result1 = methodCallA.invoke(target);
    log.info("result1={}", result1);

    // callB 메소드 정보
    Method methodCallB = classHello.getMethod("callB");
    Object result2 = methodCallB.invoke(target);
    log.info("result2={}", result2);
  }

  @Test
  void reflection2() throws Exception {
    // 클래스 정보
    Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");
    Hello target = new Hello();

    // callA 메소드 정보
    Method methodCallA = classHello.getMethod("callA");
    dynamicCall(methodCallA, target);

    // callB 메소드 정보
    Method methodCallB = classHello.getMethod("callB");
    dynamicCall(methodCallB, target);
  }

  private void dynamicCall(Method method, Object target) throws Exception {
    log.info("start");
    Object result = method.invoke(target);
    log.info("result={}", result);
  }

  @Slf4j
  static class Hello {

    public String callA() {
      log.info("callA");
      return "A";
    }

    public String callB() {
      log.info("callB");
      return "B";
    }
  }
}
