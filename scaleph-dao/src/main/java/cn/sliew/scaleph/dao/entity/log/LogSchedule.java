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

package cn.sliew.scaleph.dao.entity.log;

import java.util.Date;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 定时任务运行日志表
 * </p>
 *
 * @author liyu
 * @since 2021-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("log_schedule")
@ApiModel(value = "LogSchedule对象", description = "定时任务运行日志表")
public class LogSchedule extends BaseDO {

    private static final long serialVersionUID = -2239522121730705579L;

    @ApiModelProperty(value = "任务组")
    private String taskGroup;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "日志内容明细")
    private String traceLog;

    @ApiModelProperty(value = "任务结果")
    private String result;


}
