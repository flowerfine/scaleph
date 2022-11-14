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

package cn.sliew.scaleph.dao.entity.master.di;

import cn.sliew.scaleph.common.dict.job.JobStatus;
import cn.sliew.scaleph.common.dict.job.JobType;
import cn.sliew.scaleph.common.dict.job.RuntimeState;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据集成-作业信息
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("di_job")
@ApiModel(value = "DiJob对象", description = "数据集成-作业信息")
public class DiJob extends BaseDO {

    private static final long serialVersionUID = -4141741507654897469L;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "作业编码")
    private Long jobCode;

    @ApiModelProperty(value = "作业名称")
    private String jobName;

    @ApiModelProperty(value = "作业类型 实时、离线")
    private JobType jobType;

    @ApiModelProperty(value = "作业状态 草稿、发布、归档")
    private JobStatus jobStatus;

    @ApiModelProperty(value = "作业版本号")
    private Integer jobVersion;

    @ApiModelProperty(value = "备注")
    private String remark;

}
