package cn.sliew.scaleph.core.scheduler.service;

import cn.sliew.scaleph.common.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScheduleService {

    @Autowired
    private Scheduler scheduler;

    public void addScheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        this.scheduler.scheduleJob(jobDetail, trigger);
        log.info("schedule job {} with job group {} add success", jobDetail.getKey().getName(),
                jobDetail.getKey().getGroup());
    }

    public void deleteScheduleJob(JobKey jobKey) throws SchedulerException {
        if (checkExists(jobKey)) {
            this.scheduler.deleteJob(jobKey);
        }
    }

    public void pauseJob(JobKey jobKey) throws SchedulerException {
        if (checkExists(jobKey)) {
            scheduler.pauseJob(jobKey);
        }
    }

    public void resumeJob(JobKey jobKey) throws SchedulerException {
        if (checkExists(jobKey)) {
            scheduler.resumeJob(jobKey);
        }
    }

    public boolean checkExists(JobKey jobKey) throws SchedulerException {
        return scheduler.checkExists(jobKey);
    }

    public JobKey getJobKey(String jobName, String groupName) {
        return JobKey.jobKey(Constants.JOB_PREFIX + jobName,
                Constants.JOB_GROUP_PREFIX + groupName);
    }

    public TriggerKey getTriggerKey(String triggerName, String groupName) {
        return TriggerKey.triggerKey(Constants.TRIGGER_PREFIX + triggerName,
                Constants.TRIGGER_GROUP_PREFIX + groupName);
    }

    public void addJobListener(JobListener jobListener, Matcher<JobKey> matcher) throws SchedulerException {
        scheduler.getListenerManager().addJobListener(jobListener, matcher);
    }

}
