package com.dynamicDataSource1.second.repositories;

import com.dynamicDataSource1.second.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zbl on 2017/9/5.
 */
public interface StudentRepository extends JpaRepository<Student, Long> {
}
