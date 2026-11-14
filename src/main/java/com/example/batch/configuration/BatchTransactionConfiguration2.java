package com.example.batch.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@EnableTransactionManagement
//@Configuration
public class BatchTransactionConfiguration2 {

    // 1. 배치 메타데이터용 DataSource
    @Bean(name = "batchDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.batch")
    DataSource batchDataSource() {
        return DataSourceBuilder.create().build();
    }

    // 2. 배치 메타데이터용 TransactionManager
    @Bean(name = "batchTransactionManager")
    PlatformTransactionManager batchTransactionManager(
            @Qualifier("batchDataSource") DataSource batchDataSource) {
        return new DataSourceTransactionManager(batchDataSource);
    }

    // 3. 비즈니스 로직용 Primary DataSource
    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource.main")
    DataSource mainDataSource() {
        return DataSourceBuilder.create().build();
    }

    // 4. 비즈니스 로직용 Primary TransactionManager
    @Primary
    @Bean(name = "transactionManager")
    PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource mainDataSource) {
        return new DataSourceTransactionManager(mainDataSource);
    }
}

