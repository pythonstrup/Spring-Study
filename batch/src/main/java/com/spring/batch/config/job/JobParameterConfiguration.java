package com.spring.batch.config.job;

import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
//@Configuration
@RequiredArgsConstructor
public class JobParameterConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

//  @Bean
  public Job job() {
    return jobBuilderFactory.get("job")
        .start(step1())
        .next(step2())
        .build();
  }

//  @Bean
  public Step step1() {
    return stepBuilderFactory.get("step1")
        .tasklet((contribution, chunkContext) -> {
          System.out.println("step1 was executed");
          return RepeatStatus.FINISHED;
        }).build();
  }

//  @Bean
  public Step step2() {
    return stepBuilderFactory.get("step2")
        .tasklet((contribution, chunkContext) -> {
          System.out.println("step2 was executed");
          return RepeatStatus.FINISHED;
        }).build();
  }

//    @Bean
  public Step jobParameters() {
    return stepBuilderFactory.get("jobParameters")
        .tasklet((StepContribution contribution, ChunkContext chunkContext) -> {

          JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();
          String name = jobParameters.getString("name");
          Long seq = jobParameters.getLong("seq");
          Date date = jobParameters.getDate("date");
          Double age = jobParameters.getDouble("age");
          log.info("name={}, seq={}, date={}, age={}", name, seq, date, age);

          // chunkContext는 contribution으로 받는 것과는 다르게 Map으로 반환.. 위의 방식을 지향
          Map<String, Object> jobParametersMap = chunkContext.getStepContext().getJobParameters();
          jobParametersMap.get("name");
          jobParametersMap.get("seq");
          jobParametersMap.get("date");
          jobParametersMap.get("age");

          System.out.println("step1 was executed");
          return RepeatStatus.FINISHED;
        }).build();
  }
}
