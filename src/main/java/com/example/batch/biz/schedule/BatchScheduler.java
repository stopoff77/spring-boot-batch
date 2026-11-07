package com.example.batch.biz.schedule;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Component
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job         customTaskletJob; // CustomTaskletJobConfiguration에 정의된 Job 빈 이름


    public BatchScheduler(JobLauncher jobLauncher, @Qualifier("customJob") Job customTaskletJob) {
        this.jobLauncher      = jobLauncher;
        this.customTaskletJob = customTaskletJob;
    }


    // 예: 앱이 켜지고 5초 후, 혹은 특정 시간에 실행
    @Scheduled(fixedDelay = 10000) // 10초마다 실행 예시
    public void runJob() {
        try {
            log.info("########## Triggering Batch Job via Scheduler ##########");

            // 배치가 실행될 때마다 고유한 파라미터를 주어야 재실행이 가능합니다.
            var jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(customTaskletJob, jobParameters);

        } catch (Exception e) {
            log.error("Batch Job execution failed", e);
        }
    }
}

