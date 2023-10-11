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

package cn.sliew.scaleph.dao.entity.master.dataservice;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 数据服务 请求参数映射
 * </p>
 */
@Data
@TableName("dataservice_parameter_mapping")
@Schema(name = "DataserviceParameterMapping对象", description = "数据服务 请求参数映射")
public class DataserviceParameterMapping extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "请求参数集id")
    @TableField("parameter_map_id")
    private Long parameterMapId;

    @Schema(description = "属性")
    @TableField("property")
    private String property;

    @Schema(description = "java 类型")
    @TableField("java_type")
    private String javaType;

    @Schema(description = "jdbc 类型")
    @TableField("jdbc_type")
    private String jdbcType;

    @Schema(description = "类型转换器")
    @TableField("type_handler")
    private String typeHandler;

}
