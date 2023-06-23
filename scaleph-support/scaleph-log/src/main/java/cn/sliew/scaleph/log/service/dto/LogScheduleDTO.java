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

package cn.sliew.scaleph.log.service.dto;

import java.util.Date;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.system.model.BaseDTO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "定时任务日志信息", description = "定时任务运行日志表")
public class LogScheduleDTO extends BaseDTO {

    private static final long serialVersionUID = 1976884925111874156L;

    @Schema(description = "任务组")
    private String taskGroup;

    @Schema(description = "任务名称")
    private String taskName;

    @Schema(description = "开始时间")
    private Date startTime;

    @Schema(description = "结束时间")
    private Date endTime;

    @Schema(description = "日志内容明细")
    private String traceLog;

    @Schema(description = "任务结果")
    private DictVO result;

    public LogScheduleDTO appendLog(String raw) {
        StringBuilder builder = new StringBuilder(StrUtil.blankToDefault(this.traceLog, ""));
        builder.append(DateUtil.format(new Date(), Constants.MS_DATETIME_FORMAT))
            .append("\t")
            .append(raw)
            .append("\n");
        this.setTraceLog(builder.toString());
        return this;
    }

}
