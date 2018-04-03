package com.study.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;

/**
 * Created by zbl on 2017/6/20.
 */
public abstract class Task implements Serializable {
    protected final static Logger logger = LoggerFactory.getLogger("");
    private static final long serialVersionUID = 1118622963021986089L;

    public abstract void executeTask(ApplicationContext context);

    public <T> T getService(ApplicationContext context, Class<T> clazz) {
        return context.getBean(clazz);
    }
}
