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
 * 数据集成-作业连线2
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("di_job_link2")
@ApiModel(value = "DiJobLink2对象", description = "数据集成-作业连线2")
public class DiJobLink2 extends BaseDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("作业id")
    @TableField("job_id")
    private Long jobId;

    @ApiModelProperty("源步骤id")
    @TableField("from_step_id")
    private Long fromStepId;

    @ApiModelProperty("目标步骤id")
    @TableField("to_step_id")
    private Long toStepId;
}
