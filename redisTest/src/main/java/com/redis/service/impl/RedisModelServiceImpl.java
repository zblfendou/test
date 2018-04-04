package com.redis.service.impl;

import com.redis.models.RedisModel;
import com.redis.service.RedisModelService;
import com.redis.service.dao.RedisModelDao;
import com.redis.utils.RedisKeies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class RedisModelServiceImpl implements RedisModelService {
    @Inject
    private RedisModelDao dao;
    private static final Logger logger = LoggerFactory.getLogger(RedisModelServiceImpl.class);

    @Override
    @Transactional
//    @CachePut(value = RedisKeies.REDIS_MODEL, keyGenerator = "customGenerator",unless = "#redisModel eq null")
    @CachePut(value = RedisKeies.REDIS_MODEL, key="#redisModel.redisKey",unless = "#redisModel eq null")
    public RedisModel save(RedisModel redisModel) {
        return dao.save(redisModel);
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
