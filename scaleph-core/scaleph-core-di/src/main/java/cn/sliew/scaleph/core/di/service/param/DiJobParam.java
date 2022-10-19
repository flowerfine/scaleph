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

import cn.sliew.scaleph.common.param.PaginationParam;
import cn.sliew.scaleph.dao.entity.master.di.DiJob;
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
//        job.setProjectId(this.projectId);
//        job.setJobCode(this.jobCode);
//        job.setJobName(this.jobName);
//        job.setJobType(this.jobType);
//        job.setRuntimeState(this.runtimeState);
//        job.setJobStatus(this.jobStatus);
//        job.setDirectoryId(this.directoryId);
        return job;
    }

}
