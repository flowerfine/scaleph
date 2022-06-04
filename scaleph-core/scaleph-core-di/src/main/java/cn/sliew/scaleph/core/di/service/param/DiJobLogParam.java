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
import lombok.Data;

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
