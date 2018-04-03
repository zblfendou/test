package com.study.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zbl on 2017/6/20.
 */
public abstract class TimedTask extends Task {
    private static final long serialVersionUID = -4922057980444575090L;
    public static final Logger logger = LoggerFactory.getLogger(TimedTask.class);
    private Date startTime;

    public abstract String getJobName();

    public abstract String getJobGroup();

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
