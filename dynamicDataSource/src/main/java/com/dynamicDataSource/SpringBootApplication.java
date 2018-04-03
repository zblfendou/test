package com.dynamicDataSource;

import com.dynamicDataSource.config.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by zbl on 2017/8/29.
 */
@Configuration
@EnableAutoConfiguration(exclude = {
        JmxAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class,
        PersistenceExceptionTranslationAutoConfiguration.class})
@ComponentScan("com.dynamicDataSource")
@EnableJpaRepositories("com.dynamicDataSource.**.repositories")
@EntityScan("com.dynamicDataSource.**.models")
@EnableTransactionManagement(order = 3)
@EnableCaching(order = 2)
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@Import({DynamicDataSourceRegister.class})
public class SpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplication.class, args);
        System.out.println("Services Started");
    }
}
