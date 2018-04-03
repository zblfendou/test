package com.redis.service;

import com.redis.models.RedisModel;

public interface RedisModelService {
    void save(RedisModel redisModel);

    RedisModel get(String redisKey);

    void delete(String redisKey);
}
