package com.redis.service;

import com.redis.models.User;

public interface UserService {
    User save(User user);

    User get(long id);

    User get(String name);

    void delete(long id);

    void delete(String name);
}
