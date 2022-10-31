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

package cn.sliew.scaleph.core.di.service.vo;

import javax.validation.constraints.NotNull;
import java.util.List;

import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "作业启动参数", description = "作业启动参数")
public class DiJobRunVO {

    @NotNull
    @ApiModelProperty("job id")
    private Long jobId;

    @NotNull
    @ApiModelProperty("cluster id")
    private Long clusterId;

    @ApiModelProperty("resources")
    private List<DictVO> resources;

    public DiJobDTO toDto() {
        DiJobDTO dto = new DiJobDTO();
        dto.setId(this.jobId);
        dto.setClusterId(this.clusterId);
        return dto;
    }
}
