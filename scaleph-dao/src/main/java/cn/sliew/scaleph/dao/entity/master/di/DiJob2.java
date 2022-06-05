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

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据集成-作业信息2
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("di_job2")
@ApiModel(value = "DiJob2对象", description = "数据集成-作业信息2")
public class DiJob2 extends BaseDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("项目id")
    @TableField("project_id")
    private Long projectId;

    @ApiModelProperty("作业目录")
    @TableField("directory_id")
    private Long directoryId;

    @ApiModelProperty("作业名称")
    @TableField("job_name")
    private String jobName;

    @ApiModelProperty("作业类型")
    @TableField("job_type")
    private String jobType;

    @ApiModelProperty("负责人")
    @TableField("job_owner")
    private String jobOwner;

    @ApiModelProperty("作业状态 草稿、发布、归档")
    @TableField("job_status")
    private String jobStatus;

    @ApiModelProperty("运行状态")
    @TableField("runtime_state")
    private String runtimeState;

    @ApiModelProperty("作业版本号")
    @TableField("job_version")
    private Integer jobVersion;

    @ApiModelProperty("集群id")
    @TableField("cluster_id")
    private Integer clusterId;

    @ApiModelProperty("作业调度crontab表达式")
    @TableField("job_crontab")
    private String jobCrontab;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

}
