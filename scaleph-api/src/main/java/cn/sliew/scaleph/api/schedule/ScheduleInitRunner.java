package cn.sliew.scaleph.api.schedule;

import lombok.extern.slf4j.Slf4j;
import org.quartz.impl.matchers.EverythingMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author gleiyu
 */
@Slf4j
@Component
@Order(2)
public class ScheduleInitRunner implements ApplicationRunner {

    @Autowired
    private ScheduleService scheduleService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        scheduleService.addJobListener(new QuartzJobListener(), EverythingMatcher.allJobs());
//        initSysInternalJob();
    }

//    private void initMetaDataSyncJob() throws SchedulerException {
//        List<DataSourceMetaDTO> dataSourceDTOList = this.dataSourceMetaService.selectAll();
//        for (DataSourceMetaDTO d : dataSourceDTOList) {
//            scheduleService.updateMetaDataSyncJob(d);
//        }
//    }

//    private void initSysInternalJob() throws SchedulerException {
//        JobKey backupExpiryActiveCodeJobKey = scheduleService.getJobKey("backupExpiryActiveCode", Constants.INTERNAL_GROUP);
//        JobDetail backupExpiryActiveCodeJob = JobBuilder.newJob(AccountActiveCodeJob.class)
//                .withIdentity(backupExpiryActiveCodeJobKey)
//                .storeDurably()
//                .build();
//        Trigger simpleOneClockEveryDayTrigger = TriggerBuilder.newTrigger()
//                .withIdentity(scheduleService.getTriggerKey("backupExpiryActiveCode", Constants.INTERNAL_GROUP))
//                .withSchedule(CronScheduleBuilder.cronSchedule(Constants.CRON_ONE_CLOCK_EVERY_DAY))
//                .build();
//        if (scheduleService.checkExists(backupExpiryActiveCodeJobKey)) {
//            scheduleService.deleteScheduleJob(backupExpiryActiveCodeJobKey);
//        }
//        scheduleService.addScheduleJob(backupExpiryActiveCodeJob, simpleOneClockEveryDayTrigger);
//    }

}
