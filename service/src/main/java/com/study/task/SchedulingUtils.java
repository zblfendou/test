package com.study.task;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import javax.inject.Inject;
import javax.inject.Named;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by zbl on 2017/6/21.
 */
@Named
@DependsOn("dataInitializer")
public class SchedulingUtils {

    private static final Logger logger = LoggerFactory.getLogger(SchedulingUtils.class);
    private static final ObjectMapper mapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY).configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Inject
    @Named("commons-scheduler")
    private Scheduler scheduler;

    public void cancelTimedTaskSchedule(TimedTask job) {
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
        try {
            scheduler.unscheduleJob(triggerKey);
            logger.debug("取消定时任务成功,任务名:{},任务组:{}",job.getJobName(),job.getJobGroup());
        } catch (SchedulerException e) {
            e.printStackTrace();
            logger.error("取消任务失败,任务组和任务名为{},{}",job.getJobGroup(),job.getJobName(),", 异常信息:{}",e);
        }
    }

    public void addTimedTaskSchedule(TimedTask job) {
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
        try {
            Trigger trigger = scheduler.getTrigger(triggerKey);
            JobDetail jobDetail = JobBuilder.newJob(MyJobDetail.class).withIdentity(job.getJobName(), job.getJobGroup()).build();

            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            jobDataMap.put("className", job.getClass().getTypeName());
            jobDataMap.put("timedTask",mapper.writeValueAsString(job));

            //不存在,创建一个trigger
            if (trigger==null){
                trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(),job.getJobGroup()).startAt(job.getStartTime()).build();
                scheduler.scheduleJob(jobDetail,trigger);
            }else{
                scheduler.unscheduleJob(triggerKey);
                this.addTimedTaskSchedule(job);
            }
            logger.debug("添加定时任务成功,任务名:{},任务组:{}",job.getJobName(),job.getJobGroup());
        } catch (SchedulerException | JsonProcessingException e) {
            logger.error("安排任务失败,失败原因,{}",e);
        }
    }
}
