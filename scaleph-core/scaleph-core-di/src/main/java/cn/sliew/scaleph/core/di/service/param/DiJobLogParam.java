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

import java.util.Date;

import cn.sliew.scaleph.common.param.PaginationParam;
import cn.sliew.scaleph.dao.entity.master.di.DiJobLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DiJobLogParam extends PaginationParam {

    @ApiModelProperty("project id")
    private Long projectId;

    @ApiModelProperty("cluster id")
    private Long clusterId;

    @ApiModelProperty("job code")
    private String jobCode;

    @ApiModelProperty("start time")
    private Long startTime;

    @ApiModelProperty("end time")
    private Long endTime;

    @ApiModelProperty("job instance state")
    private String jobInstanceState;

    @ApiModelProperty("job instance id")
    private String jobInstanceId;

    @ApiModelProperty("job type")
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
