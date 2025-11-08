package com.weixf.quartz;


import com.weixf.quartz.schedule.MajorJob;
import com.weixf.quartz.utils.QuartzUtils;
import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import java.util.Date;

public class QuartzTest {

    private final static String jobName = "jobName-1";
    private final static String triggerName = "triggerName-1";
    private final static String jobGroupName = "job-group";
    private final static String triggerGroupName = "trigger-group";

    @Test
    public void simpleTest() throws SchedulerException, InterruptedException {
// 构建定时任务
        JobDetail jobDetail = JobBuilder.newJob(MajorJob.class)
                .withIdentity(jobName, jobGroupName)
                .usingJobData("jobName", "QuartzDemo")
                .build();
        // 将mapper放入jobDetail的jobDataMap中
        // jobDetail.getJobDataMap().put("personMapper", personMapper);

        Date start = new Date();
        start.setTime(start.getTime() + 10000);
        Date end = new Date();
        end.setTime(end.getTime() + 90000);

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName, triggerGroupName)
                .startNow()  // 定义开始时间
                .endAt(end)  // 定义结束时间
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(10, 10))
                .build();
        QuartzUtils.startJob(jobDetail, trigger);
    }

}
