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

package cn.sliew.scaleph.dao.entity.master.ds;

import cn.sliew.scaleph.common.dict.job.DataSourceType;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * data source type
 * </p>
 */
@Data
@EqualsAndHashCode
@TableName("ds_type")
@ApiModel(value = "DsType对象", description = "data source type")
public class DsType extends BaseDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("name")
    @TableField("`type`")
    private DataSourceType type;

    @ApiModelProperty("logo")
    @TableField("logo")
    private String logo;

    @ApiModelProperty("order")
    @TableField("`order`")
    private Integer order;

    @ApiModelProperty("remark")
    @TableField("remark")
    private String remark;

}
