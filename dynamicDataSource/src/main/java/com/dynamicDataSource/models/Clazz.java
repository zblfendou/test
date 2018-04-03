package com.dynamicDataSource.models;

import com.base.models.BaseModel;

import javax.persistence.Entity;

/**
 * Created by zbl on 2017/8/29.
 */
@Entity
public class Clazz extends BaseModel {
    private static final long serialVersionUID = 6807565002415584571L;
    private String clazzName;

    public Clazz() {
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }
}
