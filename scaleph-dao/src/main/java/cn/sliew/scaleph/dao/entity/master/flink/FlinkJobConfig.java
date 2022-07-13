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

package cn.sliew.scaleph.dao.entity.master.flink;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * flink job
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flink_job_config")
@ApiModel(value = "FlinkJobConfig对象", description = "flink job")
public class FlinkJobConfig extends BaseDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("类型。0: artifact, 1: sql+udf")
    @TableField("`type`")
    private String type;

    @ApiModelProperty("名称")
    @TableField("`name`")
    private String name;

    @ApiModelProperty("flink 集群配置 ID")
    @TableField("flink_cluster_config_id")
    private Long flinkClusterConfigId;

    @ApiModelProperty("任务自身 配置参数")
    @TableField("job_config")
    private String jobConfig;

    @ApiModelProperty("flink 配置参数")
    @TableField("flink_config")
    private String flinkConfig;

    @ApiModelProperty("版本号")
    @TableField("version")
    private Integer version;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;
}
