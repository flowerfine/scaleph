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
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 数据集成-作业步骤信息2
 * </p>
 *
 * @author wangqi
 * @since 2022-06-04
 */
@Getter
@Setter
@TableName("di_job_step2")
@ApiModel(value = "DiJobStep2对象", description = "数据集成-作业步骤信息2")
public class DiJobStep2 extends BaseDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("作业id")
    @TableField("job_id")
    private Long jobId;

    @ApiModelProperty("步骤编码")
    @TableField("step_code")
    private String stepCode;

    @ApiModelProperty("步骤标题")
    @TableField("step_title")
    private String stepTitle;

    @ApiModelProperty("步骤类型")
    @TableField("step_type")
    private String stepType;

    @ApiModelProperty("步骤名称")
    @TableField("step_name")
    private String stepName;

    @ApiModelProperty("步骤属性")
    @TableField("step_props")
    private String stepProps;

    @ApiModelProperty("步骤资源")
    @TableField("step_resources")
    private String stepResources;

    @ApiModelProperty("步骤数据源 id")
    @TableField("step_datasource_id")
    private Long stepDatasourceId;

    @ApiModelProperty("x坐标")
    @TableField("position_x")
    private Integer positionX;

    @ApiModelProperty("y坐标")
    @TableField("position_y")
    private Integer positionY;

}
