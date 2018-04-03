package com.dynamicDataSource1.second.models;

import com.base.models.BaseModel;

import javax.persistence.Entity;

/**
 * Created by zbl on 2017/9/5.
 */
@Entity
public class Student extends BaseModel {
    private static final long serialVersionUID = -6782192966017834803L;
    private String studentName;
    private String studentPassword;

    public Student() {
    }

    public String getStudentPassword() {
        return studentPassword;
    }

    public void setStudentPassword(String studentPassword) {
        this.studentPassword = studentPassword;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
