package com.redis.service.dao;

import com.redis.models.RedisModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedisModelDao extends JpaRepository<RedisModel,String> {
}
