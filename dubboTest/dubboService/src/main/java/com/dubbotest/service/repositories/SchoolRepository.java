package com.dubbotest.service.repositories;

import com.dubbotest.service.models.School;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zbl on 2017/8/28.
 */
public interface SchoolRepository extends JpaRepository<School, Long> {
}
