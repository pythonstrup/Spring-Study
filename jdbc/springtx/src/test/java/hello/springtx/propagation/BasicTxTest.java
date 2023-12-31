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
}
