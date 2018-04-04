package com.redis.service.impl;

import com.redis.models.User;
import com.redis.service.UserService;
import org.springframework.cache.annotation.CacheConfig;

import javax.inject.Named;

@Named
@CacheConfig(cacheNames = "user")
public class UserServiceImpl implements UserService {
    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public User get(long id) {
        return null;
    }

    @Override
    public User get(String name) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public void delete(String name) {

    }
}
