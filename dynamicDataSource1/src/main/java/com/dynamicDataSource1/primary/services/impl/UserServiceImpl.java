package com.dynamicDataSource1.primary.services.impl;

import com.dynamicDataSource1.primary.models.User;
import com.dynamicDataSource1.primary.repositories.UserRepository;
import com.dynamicDataSource1.primary.services.UserService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by zbl on 2017/9/5.
 */
@Named
public class UserServiceImpl implements UserService {
    @Inject
    private UserRepository userDao;
    @Override
    @Transactional
    public User add(User user) {
        return userDao.save(user);
    }
}
