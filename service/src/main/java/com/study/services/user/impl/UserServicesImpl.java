package com.study.services.user.impl;

import com.study.aspect.EventCutPoint;
import com.study.models.user.User;
import com.study.repositories.UserRepository;
import com.study.services.user.UserService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by zbl on 2017/6/20.
 */
@Named("userService")
public class UserServicesImpl implements UserService {
    @Inject
    private UserRepository userDao;

    @Override
    @Transactional
    @EventCutPoint
    public User save(User user) {
        return userDao.save(user);
    }

}
