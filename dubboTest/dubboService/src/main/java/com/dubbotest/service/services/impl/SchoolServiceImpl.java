package com.dubbotest.service.services.impl;

import com.dubbotest.client.models.Clazz;
import com.dubbotest.client.service.ClazzService;
import com.dubbotest.service.models.School;
import com.dubbotest.service.repositories.SchoolRepository;
import com.dubbotest.service.services.SchoolService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by zbl on 2017/8/28.
 */
@Named("schoolService")
public class SchoolServiceImpl implements SchoolService {
    @Inject
    private SchoolRepository schoolDao;
    @Inject
    private ClazzService clazzService;

    @Override
    @Transactional
    public School addSchool(School school) {
        return schoolDao.save(school);
    }

    @Override
    @Transactional
    public Clazz addClazz(Clazz clazz) {
        return clazzService.add(clazz);
    }
}
