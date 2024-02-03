package com.spring.batch.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobRepositoryListener implements JobExecutionListener {

  private final JobRepository jobRepository;

  @Override
  public void beforeJob(final JobExecution jobExecution) {

  }

  @Override
  public void afterJob(final JobExecution jobExecution) {
    String jobName = jobExecution.getJobInstance().getJobName();

    JobParameters jobParameters = new JobParametersBuilder()
        .addString("requestDate", "20230203").toJobParameters();

    JobExecution lastJobExecution = jobRepository.getLastJobExecution(jobName, jobParameters);
    if (lastJobExecution != null) {
      for (StepExecution execution: lastJobExecution.getStepExecutions()) {
        BatchStatus status = execution.getStatus();
        System.out.println("status = " + status);
        ExitStatus exitStatus = execution.getExitStatus();
        System.out.println("exitStatus = " + exitStatus);
        String stepName = execution.getStepName();
        System.out.println("stepName = " + stepName);
      }
    }
  }
}
