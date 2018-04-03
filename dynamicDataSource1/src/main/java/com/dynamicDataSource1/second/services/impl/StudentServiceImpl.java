package com.dynamicDataSource1.second.services.impl;

import com.dynamicDataSource1.second.models.Student;
import com.dynamicDataSource1.second.repositories.StudentRepository;
import com.dynamicDataSource1.second.services.StudentService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by zbl on 2017/9/5.
 */
@Named
public class StudentServiceImpl implements StudentService {
    @Inject
    private StudentRepository studentDao;

    @Override
    @Transactional
    public Student add(Student student) {
        return studentDao.save(student);
    }
}
