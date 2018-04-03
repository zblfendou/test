package com.dynamicDataSource.repositories;

import com.dynamicDataSource.models.School;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zbl on 2017/8/29.
 */
public interface SchoolRepository extends JpaRepository<School, Long> {
}
