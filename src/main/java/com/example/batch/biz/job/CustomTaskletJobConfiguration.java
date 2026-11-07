package com.example.batch.biz.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CustomTaskletJobConfiguration {

    @Bean
    Job customJob(JobRepository jobRepository, Step customStep) {
        log.info("customJob {} {}", jobRepository, customStep);

        return new JobBuilder("customTaskletJob", jobRepository)
                .start(customStep)
                .build();
    }

    @Bean
    Step customStep(JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            CustomTasklet customTasklet) {

        log.info("customStep {} {}", jobRepository, customTasklet);
        return new StepBuilder("customTaskletStep", jobRepository)
                .tasklet(customTasklet, transactionManager)
                .build();
    }
}
