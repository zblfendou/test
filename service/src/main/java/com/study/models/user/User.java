package com.study.models.user;

import com.base.models.BaseModel;

import javax.persistence.Entity;

/**
 * Created by zbl on 2017/6/20.
 */
@Entity
public class User extends BaseModel {
    private static final long serialVersionUID = -5315664924027679357L;
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
