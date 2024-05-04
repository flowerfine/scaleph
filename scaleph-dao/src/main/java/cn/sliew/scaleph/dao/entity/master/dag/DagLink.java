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

package cn.sliew.scaleph.dao.entity.master.dag;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * DAG 连线
 */
@Data
@TableName("dag_link")
public class DagLink extends BaseDO {

    private static final long serialVersionUID = 1L;

    @TableField("dag_instance_id")
    private Long dagInstanceId;

    @TableField("dag_config_link_id")
    private Long dagConfigLinkId;

    @TableField("instance_id")
    private String instanceId;

    @TableField("inputs")
    private String inputs;

    @TableField("outputs")
    private String outputs;

    @TableField("`status`")
    private String status;

    @TableField("start_time")
    private Date startTime;

    @TableField("end_time")
    private Date endTime;
}
