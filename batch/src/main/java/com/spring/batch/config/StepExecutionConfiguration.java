package com.spring.batch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StepExecutionConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job job() {
    return jobBuilderFactory.get("job")
        .start(step1())
        .next(step2())
        .next(step3())
        .build();
  }

  @Bean
  public Step step1() {
    return stepBuilderFactory.get("step1")
        .tasklet(((contribution, chunkContext) -> {
          log.info(">> step1 has executed");
          return RepeatStatus.FINISHED;
        }))
        .build();
  }

  @Bean
  public Step step2() {
    return stepBuilderFactory.get("step2")
        .tasklet(((contribution, chunkContext) -> {
          log.info(">> step2 has executed");
//          throw new RuntimeException("step2 has failed"); // Step2에서 실패해버리면 Step3는 실행되지도 않는다.
          return RepeatStatus.FINISHED; // Step2에서 실패한 상태가 DB에 담겨있을 때, 다시 실행하면 Step1은 실행되지 않고 Step2와 Step3만 실행된다.
        }))
        .build();
  }

  @Bean
  public Step step3() {
    return stepBuilderFactory.get("step3")
        .tasklet(((contribution, chunkContext) -> {
          log.info(">> step3 has executed");
          return RepeatStatus.FINISHED;
        }))
        .build();
  }
}
