package cn.sliew.scaleph.engine.seatunnel.service.impl;

import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.core.di.service.vo.DiJobRunVO;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelJobService;
import lombok.SneakyThrows;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class SeatunnelFlinkJob extends QuartzJobBean {

    @Autowired
    private SeatunnelJobService seatunnelJobService;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        DiJobDTO job = (DiJobDTO) dataMap.get(Constants.JOB_PARAM_JOB_INFO);
        DiJobRunVO jobRunParam = new DiJobRunVO();
        jobRunParam.setJobId(job.getId());
        jobRunParam.setClusterId(job.getClusterId());
        seatunnelJobService.submit(jobRunParam);
    }
}
