package com.example.batch.biz.job;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class BatchExecutionRunner implements ApplicationRunner {

    private final JobOperator jobOperator;
    private final Job         customTaskletJob; // CustomTaskletJobConfiguration에 선언된 Job 빈

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("########## Starting customTaskletJob via ApplicationRunner ##########");

        // 스프링 배치는 동일한 파라미터로 재실행할 수 없으므로 현재 시간을 파라미터로 부여
        var jobParameters = new JobParametersBuilder()
                .addLong("runTime", System.currentTimeMillis())
                .toJobParameters();

        jobOperator.run(customTaskletJob, jobParameters);
    }
}
