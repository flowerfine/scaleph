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
    }

}
