package com.dynamicDataSource1.primary.models;

import com.base.models.BaseModel;

import javax.persistence.Entity;

/**
 * Created by zbl on 2017/9/5.
 */
@Entity
public class User extends BaseModel {
    private static final long serialVersionUID = -4062565275335608037L;
    private String userName;
    private String password;

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
