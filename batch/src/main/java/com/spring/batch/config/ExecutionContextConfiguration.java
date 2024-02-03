package com.spring.batch.config;

import com.spring.batch.tasklet.ExecutionContextTasklet1;
import com.spring.batch.tasklet.ExecutionContextTasklet2;
import com.spring.batch.tasklet.ExecutionContextTasklet3;
import com.spring.batch.tasklet.ExecutionContextTasklet4;
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
//@Configuration
@RequiredArgsConstructor
public class ExecutionContextConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  // Tasklet
  private final ExecutionContextTasklet1 executionContextTasklet1;
  private final ExecutionContextTasklet2 executionContextTasklet2;
  private final ExecutionContextTasklet3 executionContextTasklet3;
  private final ExecutionContextTasklet4 executionContextTasklet4;

//  @Bean
  public Job job() {
    return jobBuilderFactory.get("job")
        .start(step1())
        .next(step2())
        .next(step3())
        .next(step4())
        .build();
  }

//  @Bean
  public Step step1() {
    return stepBuilderFactory.get("step1")
        .tasklet(executionContextTasklet1)
        .build();
  }

//  @Bean
  public Step step2() {
    return stepBuilderFactory.get("step2")
        .tasklet(executionContextTasklet2)
        .build();
  }

//  @Bean
  public Step step3() {
    return stepBuilderFactory.get("step3")
        .tasklet(executionContextTasklet3)
        .build();
  }

//  @Bean
  public Step step4() {
    return stepBuilderFactory.get("step4")
        .tasklet(executionContextTasklet4)
        .build();
  }
}
