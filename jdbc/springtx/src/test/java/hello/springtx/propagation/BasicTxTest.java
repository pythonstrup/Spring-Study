package hello.springtx.propagation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Slf4j
@SpringBootTest
public class BasicTxTest {

  @Autowired
  PlatformTransactionManager txManager;

  @TestConfiguration
  static class Config {
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
      return new DataSourceTransactionManager(dataSource);
    }
  }

  @Test
  void commit() {
    log.info("Transaction Start");
    TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());

    log.info("Transaction Commit Start");
    txManager.commit(status);
    log.info("Transaction Commit Complete");
  }

  @Test
  void rollback() {
    log.info("Transaction Start");
    TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());

    log.info("Transaction Rollback Start");
    txManager.rollback(status);
    log.info("Transaction Rollback Complete");
  }

  @Test
  void multiple_commit() {
    log.info("Transaction1 Start");
    TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionDefinition());
    log.info("Transaction1 Commit Start");
    txManager.commit(tx1);

    log.info("Transaction2 Start");
    TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionDefinition());
    log.info("Transaction2 Commit Start");
    txManager.commit(tx2);
  }

  @Test
  void multiple_commit_rollback() {
    log.info("Transaction1 Start");
    TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionDefinition());
    log.info("Transaction1 Commit Start");
    txManager.commit(tx1);

    log.info("Transaction2 Start");
    TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionDefinition());
    log.info("Transaction2 Rollback Start");
    txManager.rollback(tx2);
  }

  @Test
  void inner_commit() {
    log.info("외부 트랜잭션 시작");
    TransactionStatus outer = txManager.getTransaction(new DefaultTransactionDefinition());
    log.info("outer.isNewTransaction()={}", outer.isNewTransaction());

    log.info("내부 트랜잭션 시작");
    TransactionStatus inner = txManager.getTransaction(new DefaultTransactionDefinition());
    log.info("inner.isNewTransaction()={}", inner.isNewTransaction());
    log.info("내부 트랜잭션 커밋");
    txManager.commit(inner); // 실제로 아무런 일도 발생하지 않는다. (외부 트랜잭션에 종속되어 있기 때문에)

    log.info("외부 트랜잭션 커밋");
    txManager.commit(outer);
  }

  @Test
  void outer_rollback() {
    log.info("외부 트랜잭션 시작");
    TransactionStatus outer = txManager.getTransaction(new DefaultTransactionDefinition());

    log.info("내부 트랜잭션 시작");
    TransactionStatus inner = txManager.getTransaction(new DefaultTransactionDefinition());
    log.info("내부 트랜잭션 커밋");
    txManager.commit(inner);

    log.info("외부 트랜잭션 롤백");
    txManager.rollback(outer);
  }

  @Test
  void inner_rollback() {
    log.info("외부 트랜잭션 시작");
    TransactionStatus outer = txManager.getTransaction(new DefaultTransactionDefinition());

    log.info("내부 트랜잭션 시작");
    TransactionStatus inner = txManager.getTransaction(new DefaultTransactionDefinition());
    log.info("내부 트랜잭션 롤백");
    txManager.rollback(inner); // rollback-only 표시

    log.info("외부 트랜잭션 커밋");
    assertThatThrownBy(() ->txManager.commit(outer))
        .isInstanceOf(UnexpectedRollbackException.class);
  }

  @Test
  @Transactional()
  void inner_rollback_requires_new() {
    log.info("외부 트랜잭션 시작");
    TransactionStatus outer = txManager.getTransaction(new DefaultTransactionDefinition());
    log.info("outer.isNewTransaction()={}", outer.isNewTransaction()); // true

    log.info("내부 트랜잭션 시작");
    DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
    definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    TransactionStatus inner = txManager.getTransaction(definition);
    log.info("inner.isNewTransaction()={}", inner.isNewTransaction()); // true => 새로운 물리 트랜잭션
    // 두번째 커넥션(내부 트랜잭션)을 사용하는 동안 첫번째 커넥션(외부 트랜잭션)은 보류 상태가 된다.

    log.info("내부 트랜잭션 롤백");
    txManager.rollback(inner); // 내부만 롤백, 내부 트랜잭션 종료 및 두번째 커넥션을 커넥션 풀에 반납
    log.info("외부 트랜잭션 커밋"); // 보류되었던 첫번째 커넥션(외부 트랜잭션)의 보류가 끝나고 다시 사용
    txManager.commit(outer); // 물리 트랜잭션이 분리되었기 때문에 문제가 발생하지 않는다.
  }
}
