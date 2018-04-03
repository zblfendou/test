package com.study.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.inject.Named;

/**
 * Created by zbl on 2017/6/21.
 */
@Named("taskExcuter")
public class TaskExecuter implements ApplicationContextAware {
    private ApplicationContext context;
    private static final Logger logger = LoggerFactory.getLogger(TaskExecuter.class);

    public void executeTask(Task timedTask) {
        logger.debug("Executing task : {}", timedTask.getClass().getName());
        timedTask.executeTask(context);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
