package com.redis;

import com.redis.config.RedisConfig;
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

@Configuration
@EnableAutoConfiguration(exclude = {
		JmxAutoConfiguration.class,
		JdbcTemplateAutoConfiguration.class,
		PersistenceExceptionTranslationAutoConfiguration.class})
@ComponentScan("com.redis")
@EntityScan("com.redis.**.models")
@EnableJpaRepositories("com.redis.**.dao")
@EnableTransactionManagement(order = 3)
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@Import({RedisConfig.class})
public class SpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootApplication.class, args);
		System.out.println("Services Started");
	}
}
