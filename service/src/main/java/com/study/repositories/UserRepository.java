package com.study.repositories;

import com.study.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zbl on 2017/6/20.
 */
public interface UserRepository extends JpaRepository<User,Long>{
}
