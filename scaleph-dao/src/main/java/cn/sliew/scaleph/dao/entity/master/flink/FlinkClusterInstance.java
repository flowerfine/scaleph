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

import cn.sliew.scaleph.common.dict.flink.FlinkClusterStatus;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * flink 集群实例
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flink_cluster_instance")
@ApiModel(value = "FlinkClusterInstance对象", description = "flink 集群实例")
public class FlinkClusterInstance extends BaseDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("集群配置")
    @TableField("flink_cluster_config_id")
    private Long flinkClusterConfigId;

    @ApiModelProperty("集群名称")
    @TableField("`name`")
    private String name;

    @ApiModelProperty("集群id")
    @TableField("cluster_id")
    private String clusterId;

    @ApiModelProperty("集群 web-ui 链接")
    @TableField("web_interface_url")
    private String webInterfaceUrl;

    @ApiModelProperty("集群状态。0: 已创建, 1: 运行中, 2: 停止")
    @TableField("`status`")
    private FlinkClusterStatus status;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

}
