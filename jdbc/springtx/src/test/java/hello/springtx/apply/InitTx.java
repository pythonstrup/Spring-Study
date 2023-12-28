package hello.springtx.apply;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
public class InitTx {

  @Autowired Hello hello;

  @Test
  void go() {
    // 초기화 코드는 스프링이 초기화 시점에 호출한다. => 직접 호출하지 않아도 시작할 때 호출된다.
  }

  @TestConfiguration
  static class InitTxTestConfig {
    @Bean
    Hello hello() {
      return new Hello();
    }
  }

  @Slf4j
  static class Hello {

    @PostConstruct
    @Transactional
    public void initV1() {
      boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
      log.info("Hello init @PostConstruct tx active={}", isActive);
    }

    @EventListener(ApplicationReadyEvent.class) // 스프링이 완전히 떴을 때, 이벤트 발생
    @Transactional
    public void initV2() {
      boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
      log.info("Hello init ApplicationReadyEvent tx active={}", isActive);
    }
  }
}
