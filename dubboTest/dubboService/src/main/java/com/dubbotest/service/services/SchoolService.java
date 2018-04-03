package com.dubbotest.service.services;

import com.dubbotest.client.models.Clazz;
import com.dubbotest.service.models.School;

/**
 * Created by zbl on 2017/8/28.
 */
public interface SchoolService {
    School addSchool(School school);

    Clazz addClazz(Clazz clazz);
}
