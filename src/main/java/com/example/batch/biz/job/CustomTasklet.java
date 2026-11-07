package com.example.batch.biz.job;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("########## Custom Tasklet Component Started ##########");

        // JobParameters 가져오기 예시
        String targetDate = (String) chunkContext.getStepContext()
                .getJobParameters().get("targetDate");
        log.info("Target Date parameter: {}", targetDate);

        // 핵심 비즈니스 로직 수행
        log.info("Processing business logic in CustomTasklet...");

        // 정상 종료 시 FINISHED 반환 (계속 반복하려면 CONTINUE)
        return RepeatStatus.FINISHED;
    }
}
