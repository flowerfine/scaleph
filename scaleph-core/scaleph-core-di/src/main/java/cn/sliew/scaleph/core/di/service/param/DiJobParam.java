/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.core.di.service.param;

import cn.sliew.scaleph.common.dict.job.JobStatus;
import cn.sliew.scaleph.common.dict.job.JobType;
import cn.sliew.scaleph.common.dict.job.RuntimeState;
import cn.sliew.scaleph.common.param.PaginationParam;
import cn.sliew.scaleph.dao.entity.master.di.DiJob;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gleiyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DiJobParam extends PaginationParam {

    @ApiModelProperty("project id")
    private Long projectId;

    @ApiModelProperty("job code")
    private String jobCode;

    @ApiModelProperty("job name")
    private String jobName;

    @ApiModelProperty("job type")
    private JobType jobType;

    @ApiModelProperty("job runtime state")
    private RuntimeState runtimeState;

    @ApiModelProperty("job status")
    private JobStatus jobStatus;

    @ApiModelProperty("directory id")
    private Long directoryId;

    public DiJob toDo() {
        DiJob job = new DiJob();
        job.setProjectId(projectId);
        job.setJobCode(jobCode);
        job.setJobName(jobName);
        job.setJobType(jobType);
        job.setRuntimeState(runtimeState);
        job.setJobStatus(jobStatus);
        job.setDirectoryId(directoryId);
        return job;
    }

}
