package com.dynamicDataSource.services.impl;

import com.dynamicDataSource.aspect.TargetDataSource;
import com.dynamicDataSource.models.School;
import com.dynamicDataSource.repositories.SchoolRepository;
import com.dynamicDataSource.services.SchoolService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by zbl on 2017/8/29.
 */
@Named
public class SchoolServiceImpl implements SchoolService {
    @Inject
    private SchoolRepository schoolDao;

    @Override
    @Transactional
    @TargetDataSource("ds1")
    public School add(School school) {
        return schoolDao.save(school);
    }

}
