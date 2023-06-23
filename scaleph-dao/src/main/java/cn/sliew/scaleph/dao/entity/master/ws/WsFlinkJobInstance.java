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

package cn.sliew.scaleph.dao.entity.master.ws;

import cn.sliew.scaleph.common.dict.flink.FlinkClusterStatus;
import cn.sliew.scaleph.common.dict.flink.FlinkJobState;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * flink job instance
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ws_flink_job_instance")
public class WsFlinkJobInstance extends BaseDO {

    private static final long serialVersionUID = 1L;

    @TableField("flink_job_code")
    private Long flinkJobCode;

    @TableField("job_id")
    private String jobId;

    @TableField("job_name")
    private String jobName;

    @TableField("`job_state`")
    private FlinkJobState jobState;

    @TableField("cluster_id")
    private String clusterId;

    @TableField("web_interface_url")
    private String webInterfaceUrl;

    @TableField("cluster_status")
    private FlinkClusterStatus clusterStatus;

    @TableField("start_time")
    private Date startTime;

    @TableField("end_time")
    private Date endTime;

    @TableField("duration")
    private Long duration;

}
