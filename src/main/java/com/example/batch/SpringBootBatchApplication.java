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

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringBootBatchApplication.class);

        // 배치에서는 웹 서버가 필요 없으므로 NONE 설정
        app.setWebApplicationType(WebApplicationType.NONE);

        // 리스너 추가 (옵션)
        app.addListeners(new ProfileLoggerListener());

        // 실행 후 context 반환
        ConfigurableApplicationContext context = app.run(args);

        // 종료 코드 계산
        int exitCode = SpringApplication.exit(context);
        log.info("Batch process finished with exitCode={}", exitCode);

        // JVM 종료
        System.exit(exitCode);
    }

}
