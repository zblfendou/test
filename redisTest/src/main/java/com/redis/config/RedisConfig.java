package com.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
    @Override
    public CacheManager cacheManager() {
        return new RedisCacheManager(getRedisTemplate());
    }

    /**
     * 定义缓存数据 key 生成策略的bean
     * 包名+类名+方法名+所有参数
     */
    @Bean
    public KeyGenerator wiselyKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder builder = new StringBuilder();
            builder.append(target.getClass().getName());
            builder.append(method.getName());
            for (Object obj : params) {
                builder.append(obj.toString());
            }
            return builder;
        };
    }

    @Bean
    public JedisPoolConfig getPoolConfig() {
        return new JedisPoolConfig();
    }

    @Bean
    public JedisConnectionFactory getConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setPoolConfig(getPoolConfig());
        return factory;
    }

    //实例化Redis- Template对象

    @Bean
    public RedisTemplate<String, Object> getRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        initDomainRedisTemplate(redisTemplate, getConnectionFactory());
        return redisTemplate;
    }

    //设置数据库存入redis的序列化方式

    private void initDomainRedisTemplate(RedisTemplate<String, Object> redisTemplate, JedisConnectionFactory connectionFactory) {
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
    }
}
