package com.spring.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 따로 JobLauncher를 실행시키지 않기 때문에 spring.batch.job.enable을 다시 true로 해줘야한다.
 */
//@Configuration
@RequiredArgsConstructor
public class JobExecutionConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

//  @Bean
  public Job BatchJob() {
    return this.jobBuilderFactory.get("Job")
        .start(step1())
        .next(step2())
        .build();
  }

//  @Bean
  public Step step1() {
    return stepBuilderFactory.get("step1")
        .tasklet((StepContribution contribution, ChunkContext chunkContext) -> {

          JobExecution jobExecution = contribution.getStepExecution().getJobExecution();
          System.out.println("jobExecution = " + jobExecution);

          System.out.println("step1 has executed");
          return RepeatStatus.FINISHED;
        })
        .build();
  }

  /**
   * 여기서 신기한 점!
   * step1은 이미 실행되었고 step2가 계속 실패해 다시 배치를 돌렸을 때, step1은 실행되지 않고 step2만 실행된다.
   * BATCH_STEP_EXECUTION을 살펴보자. => STEP_NAME과 상태값을 통해 실패한 Step부터 다시 실행하는 것처럼 보인다.
   */
//  @Bean
  public Step step2() {
    return stepBuilderFactory.get("step2")
        .tasklet((contribution, chunkContext) -> {
//          throw new RuntimeException("JobExecution has failed");
          System.out.println("step2 has executed");
          return RepeatStatus.FINISHED;
        })
        .build();
  }
}
