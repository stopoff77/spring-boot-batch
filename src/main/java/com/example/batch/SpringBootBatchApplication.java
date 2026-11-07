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

    // 계속 떠있게 하기
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringBootBatchApplication.class);

        // 리스너 추가 (옵션)
        app.addListeners(new ProfileLoggerListener());

        // 실행 후 context 반환
        app.run(args);


        log.info("########## [BATCH APPLICATION STARTED] ##########");

        // [핵심] 배치 작업 완료 후 프로세스가 즉시 종료되지 않도록 메인 스레드 대기 처리
        try {
            // 방법 1: 현재 메인 스레드를 무한 대기시킴 (서버처럼 계속 켜져 있음)
            Thread.currentThread().join();

            /*
             * 방법 2: 특정 조건이나 외부 신호(Ctrl+C 등)로 안전하게 종료하고 싶다면
             * CountDownLatch나 아래와 같은 셧다운 훅을 조합할 수도 있습니다.
             */
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
