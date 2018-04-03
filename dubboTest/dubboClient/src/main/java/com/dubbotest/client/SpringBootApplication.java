package com.dubbotest.client;

import com.base.config.AsyncConfig;
import com.base.config.RestTemplateConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by zbl on 2017/8/28.
 */
@Configuration
@EnableAutoConfiguration(exclude = {JmxAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class,
        PersistenceExceptionTranslationAutoConfiguration.class})
@ImportResource({"classpath*:clazz-dubbo.xml"})
@ComponentScan("com.dubbotest.client")
@EnableJpaRepositories("com.dubbotest.client.**.repositories")
@EntityScan("com.dubbotest.client.**.models")
@EnableTransactionManagement(order = 3)
@EnableCaching(order = 2)
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@Import({AsyncConfig.class, RestTemplateConfig.class})
public class SpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplication.class, args);
        System.out.println("Services Started");
    }
}
