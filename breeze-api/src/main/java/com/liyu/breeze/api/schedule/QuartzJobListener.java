package com.liyu.breeze.api.schedule;

import cn.hutool.core.util.StrUtil;
import com.liyu.breeze.common.constant.Constants;
import com.liyu.breeze.common.enums.TaskResultEnum;
import com.liyu.breeze.service.admin.ScheduleLogService;
import com.liyu.breeze.service.dto.admin.ScheduleLogDTO;
import com.liyu.breeze.service.util.SpringApplicationContextUtil;
import com.liyu.breeze.service.vo.DictVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.Date;

/**
 * @author gleiyu
 */
@Slf4j
public class QuartzJobListener implements JobListener {
    @Override
    public String getName() {
        return "quartz-job-listener";
    }

    @SneakyThrows
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        JobKey jobKey = jobDetail.getKey();
        JobDataMap dataMap = jobDetail.getJobDataMap();
        ScheduleLogDTO logDTO = new ScheduleLogDTO();
        logDTO.setStartTime(new Date());
        logDTO.setTaskGroup(jobKey.getGroup());
        logDTO.setTaskName(jobKey.getName());
        logDTO.appendLog(StrUtil.format("job {} in group {} begin running...", jobKey.getName(), jobKey.getGroup()));
        dataMap.put(Constants.JOB_LOG_KEY, logDTO);
        log.info("job {} in group {} begin running... ", jobKey.getName(), jobKey.getGroup());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        JobKey jobKey = context.getJobDetail().getKey();
        log.info("job {} in group {} execute vetoed", jobKey.getName(), jobKey.getGroup());
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        JobDetail jobDetail = context.getJobDetail();
        JobKey jobKey = jobDetail.getKey();
        JobDataMap dataMap = jobDetail.getJobDataMap();
        ScheduleLogDTO logDTO = (ScheduleLogDTO) dataMap.get(Constants.JOB_LOG_KEY);
        logDTO.setEndTime(new Date());
        if (jobException == null) {
            logDTO.setResult(new DictVO(TaskResultEnum.SUCCESS.getCode(), TaskResultEnum.SUCCESS.getValue()));
        } else {
            logDTO.setResult(new DictVO(TaskResultEnum.FAILURE.getCode(), TaskResultEnum.FAILURE.getValue()));
        }
        logDTO.appendLog(StrUtil.format("job {} in group {} execute completed", jobKey.getName(), jobKey.getGroup()));
        ScheduleLogService scheduleLogService = SpringApplicationContextUtil.getBean(ScheduleLogService.class);
        scheduleLogService.insert(logDTO);
        log.info("job {} in group {} execute completed", jobKey.getName(), jobKey.getGroup());
    }
}
