/**
 * Copyright 2020 장동선.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * */
package com.example.batch;

import java.util.concurrent.CountDownLatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import com.example.batch.common.context.banner.ProfileLoggerListener;
import com.example.batch.configuration.ComponentScanConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class // dataSource yml 설정시 제거
        , DataSourceTransactionManagerAutoConfiguration.class, JndiDataSourceAutoConfiguration.class,
        SecurityAutoConfiguration.class, JacksonAutoConfiguration.class
})
@SpringBootApplication(scanBasePackageClasses = { ComponentScanConfiguration.class })
public class SpringBootBatchApplication {

    // 1. 메인 스레드를 대기시키고 종료 신호를 전달할 Latch 선언
    private static final CountDownLatch latch = new CountDownLatch(1);


    // 계속 떠있게 하기 (web-application-type=NONE 인 경우)
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringBootBatchApplication.class);

        // 배치에서는 웹 서버가 필요 없으므로 NONE 설정
        app.setWebApplicationType(WebApplicationType.NONE);

        // 리스너 추가 (옵션)
        app.addListeners(new ProfileLoggerListener());

        // 실행 후 context 반환
        ConfigurableApplicationContext context = app.run(args);

        // spring.application.name
        Environment env     = context.getEnvironment();
        String      appName = env.getProperty("spring.application.name");


        log.info("########## [{} STARTED] ##########", appName);

        // 2. JVM 셧다운 훅 등록 (Ctrl+C 또는 OS 종료 시그널 감지)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("########## [SHUTDOWN SIGNAL RECEIVED (Ctrl+C / SIGINT)] ##########");

            // 여기에 DB 커넥션 정리, 임시 파일 삭제 등 안전하게 종료할 로직을 작성할 수 있습니다.

            // 래치를 카운트다운하여 메인 스레드의 대기를 풀어줌
            latch.countDown();
        }));

        try {
            // 3. 셧다운 훅이 실행되거나 누군가 latch.countDown()을 호출할 때까지 대기
            latch.await();
            log.info("########## [{} EXITED SAFELY] ##########", appName);
        } catch (InterruptedException e) {
            log.error("Main thread interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    // 바로 종료
    public static void main2(String[] args) {
        SpringApplication app = new SpringApplication(SpringBootBatchApplication.class);

        // 리스너 추가 (옵션)
        app.addListeners(new ProfileLoggerListener());

        // 실행 후 context 반환
        app.run(args);

        log.info("########## [BATCH APPLICATION DESTROYED] ##########");
    }


    /**
     * ########## ##########
     *
     * ########## ##########
     */
    // WebApplicationType - yml 설정 (spring.main.web-application-type: NONE)
    public static void main_with_exit_YML(String[] args) {
        SpringApplication app = new SpringApplication(SpringBootBatchApplication.class);

        // 리스너 추가 (옵션)
        app.addListeners(new ProfileLoggerListener());

        // 실행 후 context 반환
        ConfigurableApplicationContext context = app.run(args);

        //
        log.info("########## [START] ##########");

        // 종료 코드 계산
        int exitCode = SpringApplication.exit(context);
        log.info("Batch process finished with exitCode={}", exitCode);
        log.info("########## [END] ##########");

        // JVM 종료
        System.exit(exitCode);
    }


    // WebApplicationType - java 설정
    public static void main_with_exit_JAVA(String[] args) {
        SpringApplication app = new SpringApplication(SpringBootBatchApplication.class);

        // 배치에서는 웹 서버가 필요 없으므로 NONE 설정
        app.setWebApplicationType(WebApplicationType.NONE);

        // 리스너 추가 (옵션)
        app.addListeners(new ProfileLoggerListener());

        // 실행 후 context 반환
        ConfigurableApplicationContext context = app.run(args);

        //
        log.info("########## [START] ##########");

        // 종료 코드 계산
        int exitCode = SpringApplication.exit(context);
        log.info("Batch process finished with exitCode={}", exitCode);
        log.info("########## [END] ##########");

        // JVM 종료
        System.exit(exitCode);
    }
}
