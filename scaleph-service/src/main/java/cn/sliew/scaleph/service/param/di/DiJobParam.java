package cn.sliew.scaleph.service.param.di;

import cn.sliew.scaleph.dao.entity.master.di.DiJob;
import cn.sliew.scaleph.common.param.PaginationParam;
import lombok.Data;

/**
 * @author gleiyu
 */
@Data
public class DiJobParam extends PaginationParam {

    private Long projectId;

    private String jobCode;

    private String jobName;

    private String jobType;

    private String runtimeState;

    private String jobStatus;

    private Long directoryId;

    public DiJob toDo() {
        DiJob job = new DiJob();
        job.setProjectId(this.projectId);
        job.setJobCode(this.jobCode);
        job.setJobName(this.jobName);
        job.setJobType(this.jobType);
        job.setRuntimeState(this.runtimeState);
        job.setJobStatus(this.jobStatus);
        job.setDirectoryId(this.directoryId);
        return job;
    }

}
