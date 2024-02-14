package com.spring.batch.config;

import javax.sql.DataSource;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomBatchConfiguration extends BasicBatchConfigurer {

  private final DataSource dataSource;

  protected CustomBatchConfiguration(
      final BatchProperties properties,
      final DataSource dataSource,
      final TransactionManagerCustomizers transactionManagerCustomizers) {
    super(properties, dataSource, transactionManagerCustomizers);
    this.dataSource = dataSource;
  }

  @Override
  protected JobRepository createJobRepository() throws Exception {
    JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
    factory.setDataSource(this.dataSource);
    factory.setTransactionManager(getTransactionManager());
    factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
    factory.setTablePrefix("SYSTEM_");

    return factory.getObject();
  }
}
