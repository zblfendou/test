package seo.market;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@ComponentScan("market.seo")
@EntityScan("market.seo.**.models")
@EnableJpaRepositories("market.seo.**.daos")
@EnableAspectJAutoProxy(exposeProxy = true)
public class ServiceTesterConfig {
}
