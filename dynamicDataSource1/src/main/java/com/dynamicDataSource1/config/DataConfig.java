package com.dynamicDataSource1.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataConfig {
    @Bean(name = "primaryDS")
    @Qualifier("primaryDS")
    @Primary  //默认实现
    @ConfigurationProperties(prefix = "spring.primary.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "secondDS")
    @Qualifier("secondDS")
    @ConfigurationProperties(prefix = "spring.second.datasource")
    public DataSource secondDataSource() {
        return DataSourceBuilder.create().build();
    }
}
