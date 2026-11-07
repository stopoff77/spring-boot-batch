package com.example.batch.configuration;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.batch.BatchDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableTransactionManagement
@EnableConfigurationProperties(BatchProperties.class)
@Configuration
public class BatchTransactionConfiguration {

    // 1. HikariCP DataSource 명시적 등록 (yml의 spring.datasource 설정과 자동 바인딩)
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    DataSource dataSource() {
        log.info("Creating HikariDataSource for Spring Batch");
        return new HikariDataSource();
    }

    // 2. 트랜잭션 매니저 설정
    @Bean
    PlatformTransactionManager transactionManager(DataSource dataSource) {
        log.info("Creating DataSourceTransactionManager for Spring Batch & Application");
        return new DataSourceTransactionManager(dataSource);
    }

    // 3. Spring Batch 메타데이터 테이블 자동 생성 스크립트 실행 설정
    @Bean
    BatchDataSourceScriptDatabaseInitializer batchRepositoryInitializer(
            DataSource dataSource, BatchProperties properties) {
        return new BatchDataSourceScriptDatabaseInitializer(dataSource, properties.getJdbc());
    }
}

