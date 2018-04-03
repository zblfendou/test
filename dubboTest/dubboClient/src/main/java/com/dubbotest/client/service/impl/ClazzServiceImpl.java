package com.dubbotest.client.service.impl;

import com.dubbotest.client.models.Clazz;
import com.dubbotest.client.repositories.ClazzRepository;
import com.dubbotest.client.service.ClazzService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.TimeUnit;

/**
 * Created by zbl on 2017/8/28.
 */
@Named("clazzService")
public class ClazzServiceImpl implements ClazzService {
    @Inject
    private ClazzRepository clazzDao;

    @Override
    @Transactional
    public Clazz add(Clazz clazz) {

        Clazz save = clazzDao.save(clazz);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return save;
    }
}
