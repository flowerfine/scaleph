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

import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * flink job
 * </p>
 */
@Data
@TableName("flink_job")
@ApiModel(value = "FlinkJob对象", description = "flink job")
public class FlinkJob extends BaseDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("job type. 0: jar, 1: sql+udf, 2: seatunnel")
    @TableField("`type`")
    private FlinkJobType type;

    @ApiModelProperty("job code")
    @TableField("`code`")
    private Long code;

    @TableField("`name`")
    private String name;

    @ApiModelProperty("jar: flink_artifact_jar_id")
    @TableField("flink_artifact_id")
    private Long flinkArtifactId;

    @TableField("job_config")
    private String jobConfig;

    @TableField("flink_cluster_config_id")
    private Long flinkClusterConfigId;

    @TableField("flink_cluster_instance_id")
    private Long flinkClusterInstanceId;

    @TableField("flink_config")
    private String flinkConfig;

    @ApiModelProperty("jars")
    private String jars;

    @TableField("from_version")
    private Long fromVersion;

    @TableField("version")
    private Long version;

    @TableField("remark")
    private String remark;

}
