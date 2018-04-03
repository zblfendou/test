package com.dubbotest.client.models;

import com.base.models.BaseModel;

import javax.persistence.Entity;

/**
 * Created by zbl on 2017/8/24.
 */
@Entity
public class Clazz extends BaseModel {
    private static final long serialVersionUID = 6239632917545673425L;
    private String clazzName;

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }
}
