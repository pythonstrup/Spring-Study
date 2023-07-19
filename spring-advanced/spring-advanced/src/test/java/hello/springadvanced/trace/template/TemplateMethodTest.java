package hello.springadvanced.trace.template;

import hello.springadvanced.trace.template.code.AbstractTemplate;
import hello.springadvanced.trace.template.code.SubClassLogic1;
import hello.springadvanced.trace.template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateMethodTest {

  @Test
  void templateMethodV0() {
    logic1();
    logic2();
  }

  private void logic1() {
    long startTime = System.currentTimeMillis();

    // 비즈니스 로직 실행
    log.info("비즈니스 로직1 실행");

    // 비즈니스 로직 종료
    long endTime = System.currentTimeMillis();
    long resultTime = endTime - startTime;
    log.info("resultTime={}", resultTime);
  }

  private void logic2() {
    long startTime = System.currentTimeMillis();

    // 비즈니스 로직 실행
    log.info("비즈니스 로직2 실행");

    // 비즈니스 로직 종료
    long endTime = System.currentTimeMillis();
    long resultTime = endTime - startTime;
    log.info("resultTime={}", resultTime);
  }

  /**
   * 템플릿 메소드 패턴 적용
   */
  @Test
  void templateMethodV1() {
    AbstractTemplate template1 = new SubClassLogic1();
    template1.execute();

    AbstractTemplate template2 = new SubClassLogic2();
    template2.execute();
  }

  @Test
  void templateMethodV2() {
    AbstractTemplate template1 = new AbstractTemplate() {
      @Override
      protected void call() {
        log.info("비즈니스 로직1 실행");
      }
    };
    log.info("class1 name={}", template1.getClass()); // TemplateMethodTest$1
    // TemplateMethodTest 내부 클래스 + $1(임의의 이름)
    template1.execute();

    AbstractTemplate template2 = new AbstractTemplate() {
      @Override
      protected void call() {
        log.info("비즈니스 로직2 실행");
      }
    };
    log.info("class2 name={}", template2.getClass()); // TemplateMethodTest$2
    template2.execute();
  }
}
