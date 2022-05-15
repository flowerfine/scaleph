package cn.sliew.scaleph.service.param.di;

import cn.sliew.scaleph.dao.entity.master.di.DiJobLog;
import cn.sliew.scaleph.service.param.PaginationParam;
import lombok.Data;

import java.util.Date;

@Data
public class DiJobLogParam extends PaginationParam {

    private Long projectId;

    private Long clusterId;

    private String jobCode;

    private Long startTime;

    private Long endTime;

    private String jobInstanceState;

    private String jobInstanceId;

    private String jobType;

    public DiJobLog toDo() {
        DiJobLog log = new DiJobLog();
        log.setProjectId(this.projectId);
        log.setClusterId(this.clusterId);
        log.setJobCode(this.jobCode);
        if (this.startTime != null) {
            log.setStartTime(new Date(this.startTime));
        }
        if (this.endTime != null) {
            log.setEndTime(new Date(this.endTime));
        }
        log.setJobInstanceState(this.jobInstanceState);
        log.setJobInstanceId(this.jobInstanceId);
        return log;
    }

}
