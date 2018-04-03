package com.aop.services.impl;

import com.aop.annotation.SystemServiceLog;
import com.aop.models.User;
import com.aop.repositories.UserRepository;
import com.aop.services.UserService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by zbl on 2017/8/23.
 */
@Named("userService")
public class UserServiceImpl implements UserService {
    @Inject
    private UserRepository userDao;

    @Override
    @Transactional
    @SystemServiceLog(description = "添加测试service层")
    public User add(User user) {
        return userDao.save(user);
    }
}
