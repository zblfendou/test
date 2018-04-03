package com.aop.repositories;

import com.aop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zbl on 2017/8/23.
 */
public interface UserRepository extends JpaRepository<User,Long>{
}
