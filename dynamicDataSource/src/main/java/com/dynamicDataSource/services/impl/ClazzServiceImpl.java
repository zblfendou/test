package com.dynamicDataSource.services.impl;

import com.dynamicDataSource.models.Clazz;
import com.dynamicDataSource.repositories.ClazzRepository;
import com.dynamicDataSource.services.ClazzService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by zbl on 2017/8/29.
 */
@Named
public class ClazzServiceImpl implements ClazzService {
    @Inject
    private ClazzRepository clazzDao;

    @Override
    @Transactional
    public Clazz add(Clazz clazz) {
        return clazzDao.save(clazz);
    }
}
