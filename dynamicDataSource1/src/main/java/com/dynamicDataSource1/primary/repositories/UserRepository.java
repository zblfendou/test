package com.dynamicDataSource1.primary.repositories;

import com.dynamicDataSource1.primary.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zbl on 2017/9/5.
 */
public interface UserRepository extends JpaRepository<User,Long> {
}
