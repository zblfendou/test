package com.aop.models;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Created by zbl on 2017/8/22.
 */
@Entity
public class User extends BaseModel implements Serializable {

    private static final long serialVersionUID = -1166170937932883455L;
    private String name;
    private String pwd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
