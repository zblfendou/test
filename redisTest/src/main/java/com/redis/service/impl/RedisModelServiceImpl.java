package com.redis.service.impl;

import com.redis.models.RedisModel;
import com.redis.service.RedisModelService;
import com.redis.service.dao.RedisModelDao;
import com.redis.utils.RedisKeies;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class RedisModelServiceImpl implements RedisModelService {
    @Inject
    private RedisModelDao dao;

    @Override
    @Transactional
    @Cacheable(value = RedisKeies.REDIS_MODEL, keyGenerator = "wiselyKeyGenerator")
    public void save(RedisModel redisModel) {
        dao.save(redisModel);
    }

    @Override
    @Cacheable(value = RedisKeies.REDIS_MODEL)
    public RedisModel get(String redisKey) {
        return dao.findOne(redisKey);
    }

    @Override
    @Transactional
    @CacheEvict(value = RedisKeies.REDIS_MODEL)
    public void delete(String redisKey) {
        dao.delete(redisKey);
    }
}
