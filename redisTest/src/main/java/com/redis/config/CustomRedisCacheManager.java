package com.redis.config;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.StringUtils;

import java.util.Collection;

public class CustomRedisCacheManager extends RedisCacheManager {

    private static final String DELIMITER = "#";

    CustomRedisCacheManager(RedisOperations redisOperations) {
        super(redisOperations);
    }

    public CustomRedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames) {
        super(redisOperations, cacheNames);
    }

    public CustomRedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames, boolean cacheNullValues) {
        super(redisOperations, cacheNames, cacheNullValues);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected RedisCache createCache(String cacheName) {
        if (!(StringUtils.hasText(cacheName) && cacheName.contains(DELIMITER))) return super.createCache(cacheName);
        else {
            try {
                String[] nameAndExpired = cacheName.split(DELIMITER);
                return new RedisCache(nameAndExpired[0], (isUsePrefix() ? getCachePrefix().prefix(cacheName) : null), getRedisOperations(),
                        Long.valueOf(nameAndExpired[1]),false);
            } catch (NumberFormatException e) {
                return super.createCache(cacheName);
            }
        }
    }

}
