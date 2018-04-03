package com.dubbotest.client.repositories;

import com.dubbotest.client.models.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zbl on 2017/8/28.
 */
public interface ClazzRepository extends JpaRepository<Clazz, Long> {
}
