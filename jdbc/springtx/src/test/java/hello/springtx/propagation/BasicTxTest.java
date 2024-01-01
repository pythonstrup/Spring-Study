package hello.springtx.propagation;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
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
    txManager.commit(inner); // 실제로 아무런 일도 발생하지 않는다. (외부 트랜잭션에 종속되어 있기 때문에)

    log.info("외부 트랜잭션 롤백");
    txManager.rollback(outer);
  }
}
