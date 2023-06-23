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

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginName;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginType;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据集成-作业步骤信息
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ws_di_job_step")
public class WsDiJobStep extends BaseDO {

    private static final long serialVersionUID = -8131332626792290363L;

    private Long jobId;

    private String stepCode;

    private String stepTitle;

    private SeaTunnelPluginType stepType;

    private SeaTunnelPluginName stepName;

    private Integer positionX;

    private Integer positionY;

    private String stepAttrs;
}
