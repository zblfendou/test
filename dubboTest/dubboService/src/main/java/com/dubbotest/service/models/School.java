package com.dubbotest.service.models;

import com.base.models.BaseModel;

import javax.persistence.Entity;

/**
 * Created by zbl on 2017/8/28.
 */
@Entity
public class School extends BaseModel {
    private static final long serialVersionUID = -4984766322936163425L;
    private String schoolName;
    private String schoolAddress;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }
}
