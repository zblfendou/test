package com.study.controller;

import com.study.task.SchedulingUtils;
import com.study.task.TestTask;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Calendar;

/**
 * Created by zbl on 2017/6/21.
 */
@RestController
@RequestMapping("/task")
public class TaskTestController {
    @Inject
    private SchedulingUtils schedulingUtils;

    @RequestMapping("/add.do")
    public String addTimedTask() {
        TestTask task = new TestTask();
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, 10);
        task.setStartTime(instance.getTime());
        schedulingUtils.addTimedTaskSchedule(task);
        return "ok";
    }
}
