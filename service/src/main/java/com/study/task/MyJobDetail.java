package com.study.task;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import java.io.IOException;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by zbl on 2017/6/21.
 */
public class MyJobDetail implements Job {
    private static final ObjectMapper mapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY).configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final static Logger logger = LoggerFactory.getLogger(TaskExecuter.class);

    @Inject
    private TaskExecuter taskExecuter;
    private String timedTask;
    private String className;

    public String getTimedTask() {
        return timedTask;
    }

    public void setTimedTask(String timedTask) {
        this.timedTask = timedTask;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (timedTask!=null){
            try {
                taskExecuter.executeTask((TimedTask)mapper.readValue(timedTask, Class.forName(className)));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                logger.error("Job execute failed. ",e);
            }
        }
    }
}
