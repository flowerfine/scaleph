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

package cn.sliew.scaleph.dao.entity.master.snowflake;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("snowflake_worker_node")
@ApiModel(value = "SnowflakeWorkerNode对象", description = "Snowflake worker node")
public class SnowflakeWorkerNode extends BaseDO {

    @ApiModelProperty("Type of CONTAINER: HostName, ACTUAL : IP.")
    @TableField("host_name")
    private String hostName;

    @ApiModelProperty("Type of CONTAINER: Port, ACTUAL : Timestamp + Random(0-10000)")
    @TableField("port")
    private String port;

    @ApiModelProperty("node type: CONTAINER(1), ACTUAL(2), FAKE(3)")
    @TableField("type")
    private int type;

    @ApiModelProperty("Worker launch date, default now")
    @TableField("launch_date")
    private Date launchDate = new Date();

}