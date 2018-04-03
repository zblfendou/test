package com.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.inject.Inject;
import javax.sql.DataSource;

import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.commaDelimitedListToSet;

/**
 * Created by zbl on 2017/6/19.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
public class DataInitializerConfig {
    @Value("${spring.ext.datainitial.sources:}")
    private String dataInitialSources;
    @Inject
    private DataSource dataSource;

    @Bean("dataInitializer")
    public DataSourceInitializer dataSourceInitializer() {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();

        dataSourceInitializer.setDataSource(dataSource);
        Resource[] sources = commaDelimitedListToSet(dataInitialSources).stream().map(String::trim).map(ClassPathResource::new).collect(toList()).toArray(new Resource[0]);
        dataSourceInitializer.setDatabasePopulator(new ResourceDatabasePopulator(true, true, "UTF-8"
                , sources));
        return dataSourceInitializer;
    }

}
